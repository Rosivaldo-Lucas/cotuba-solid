package cotuba;

import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class LeitorOpcoesCLI {

  private Path diretorioDosMD;
  private String formato;
  private Path arquivoDeSaida;
  private Boolean modoVerboso;

  public LeitorOpcoesCLI(final String[] args) {
    try {
      final Options options = new Options();

      final Option opcaoDeDiretorioDosMD = new Option("d", "dir", true, "Diretório que contém os arquivos md. Default: diretório atual.");;
      options.addOption(opcaoDeDiretorioDosMD);

      final Option opcaoDeFormatoDoEbook = new Option("f", "format", true, "Formato de saída do ebook. Pode ser: pdf ou epub. Default: pdf");
      options.addOption(opcaoDeFormatoDoEbook);

      final Option opcaoDeArquivoDeSaida = new Option("o", "output", true, "Arquivo de saída do ebook. Default: book.{formato}.");
      options.addOption(opcaoDeArquivoDeSaida);

      final Option opcaoModoVerboso = new Option("v", "verbose", false, "Habilita modo verboso.");
      options.addOption(opcaoModoVerboso);

      final CommandLineParser cmdParser = new DefaultParser();

      final HelpFormatter ajuda = new HelpFormatter();

      final CommandLine cmd;

      try {
        cmd = cmdParser.parse(options, args);
      } catch (final ParseException ex) {
        ajuda.printHelp("cotuba", options);

        throw new IllegalArgumentException("Opção inválida", ex);
      }

      final String nomeDoDiretorioDosMD = cmd.getOptionValue("dir");

      if (nomeDoDiretorioDosMD != null) {
        this.diretorioDosMD = Paths.get(nomeDoDiretorioDosMD);

        if (!Files.isDirectory(this.diretorioDosMD)) {
          throw new IllegalArgumentException(nomeDoDiretorioDosMD + " não é um diretório.");
        }
      } else {
        final Path diretorioAtual = Paths.get("");

        this.diretorioDosMD = diretorioAtual;
      }

      final String nomeDoFormatoDoEbook = cmd.getOptionValue("format");

      if (nomeDoFormatoDoEbook != null) {
        this.formato = nomeDoFormatoDoEbook.toLowerCase();
      } else {
        this.formato = "pdf";
      }

      final String nomeDoArquivoDeSaidaDoEbook = cmd.getOptionValue("output");

      if (nomeDoArquivoDeSaidaDoEbook != null) {
        this.arquivoDeSaida = Paths.get(nomeDoArquivoDeSaidaDoEbook);
      } else {
        this.arquivoDeSaida = Paths.get("book." + formato.toLowerCase());
      }

      if (Files.isDirectory(this.arquivoDeSaida)) {
        // deleta arquivos do diretório recursivamente
        Files
                .walk(this.arquivoDeSaida)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
      } else {
        Files.deleteIfExists(this.arquivoDeSaida);
      }

      this.modoVerboso = cmd.hasOption("verbose");
    } catch (final IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  public Path getDiretorioDosMD() {
    return diretorioDosMD;
  }

  public String getFormato() {
    return formato;
  }

  public Path getArquivoDeSaida() {
    return arquivoDeSaida;
  }

  public Boolean getModoVerboso() {
    return modoVerboso;
  }

}
