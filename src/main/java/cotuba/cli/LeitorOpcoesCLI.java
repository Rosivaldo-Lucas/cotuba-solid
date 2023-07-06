package cotuba.cli;

import cotuba.application.ParametrosCotuba;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

class LeitorOpcoesCLI implements ParametrosCotuba {

  private Path diretorioDosMD;
  private String formato;
  private Path arquivoDeSaida;
  private Boolean modoVerboso;

  public LeitorOpcoesCLI(final String[] args) {
    final Options options = this.criaOpcoes();

    final CommandLine cmd = this.parseDosArgumentos(args, options);

    this.trataDiretorioDosMD(cmd);
    this.trataFormato(cmd);
    this.trataArquivoDeSaida(cmd);
    this.trataModoVerboso(cmd);
  }

  private Options criaOpcoes() {
    final Options options = new Options();

    final Option opcaoDeDiretorioDosMD = new Option("d", "dir", true, "Diretório que contém os arquivos md. Default: diretório atual.");
    options.addOption(opcaoDeDiretorioDosMD);

    final Option opcaoDeFormatoDoEbook = new Option("f", "format", true, "Formato de saída do ebook. Pode ser: pdf ou epub. Default: pdf");
    options.addOption(opcaoDeFormatoDoEbook);

    final Option opcaoDeArquivoDeSaida = new Option("o", "output", true, "Arquivo de saída do ebook. Default: book.{formato}.");
    options.addOption(opcaoDeArquivoDeSaida);

    final Option opcaoModoVerboso = new Option("v", "verbose", false, "Habilita modo verboso.");
    options.addOption(opcaoModoVerboso);

    return options;
  }

  private CommandLine parseDosArgumentos(final String[] args, final Options options) {
    final CommandLineParser cmdParser = new DefaultParser();

    final HelpFormatter ajuda = new HelpFormatter();

    final CommandLine cmd;

    try {
      cmd = cmdParser.parse(options, args);
    } catch (final ParseException ex) {
      ajuda.printHelp("cotuba", options);

      throw new IllegalArgumentException("Opção inválida", ex);
    }

    return cmd;
  }

  private void trataDiretorioDosMD(final CommandLine cmd) {
    final String nomeDoDiretorioDosMD = cmd.getOptionValue("dir");

    if (nomeDoDiretorioDosMD != null) {
      this.diretorioDosMD = Paths.get(nomeDoDiretorioDosMD);

      if (!Files.isDirectory(this.diretorioDosMD)) {
        throw new IllegalArgumentException(nomeDoDiretorioDosMD + " não é um diretório.");
      }
    } else {
      this.diretorioDosMD = Paths.get("");
    }
  }

  private void trataFormato(final CommandLine cmd) {
    final String nomeDoFormatoDoEbook = cmd.getOptionValue("format");

    if (nomeDoFormatoDoEbook != null) {
      this.formato = nomeDoFormatoDoEbook.toLowerCase();
    } else {
      this.formato = "pdf";
    }
  }

  private void trataArquivoDeSaida(final CommandLine cmd) {
    final String nomeDoArquivoDeSaidaDoEbook = cmd.getOptionValue("output");

    this.arquivoDeSaida = Paths.get("book." + this.formato.toLowerCase());

    if (nomeDoArquivoDeSaidaDoEbook != null) {
      this.arquivoDeSaida = Paths.get(nomeDoArquivoDeSaidaDoEbook);
    }

    this.deletaArquivos();
  }

  private void trataModoVerboso(final CommandLine cmd) {
    this.modoVerboso = cmd.hasOption("verbose");
  }

  private void deletaArquivos() {
    try (final Stream<Path> paths = Files.walk(this.arquivoDeSaida)) {
      if (Files.isDirectory(this.arquivoDeSaida)) {
        paths
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(file -> {
                  if (!file.delete()) {
                    throw new IllegalArgumentException();
                  }
                });
      } else {
        Files.deleteIfExists(this.arquivoDeSaida);
      }
    } catch (final IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  @Override
  public String obtemFormato() {
    return this.formato;
  }

  @Override
  public Path obtemDiretorioDosMD() {
    return this.diretorioDosMD;
  }

  @Override
  public Path obtemArquivoDeSaida() {
    return this.arquivoDeSaida;
  }

  public Boolean obtemModoVerboso() {
    return modoVerboso;
  }

}
