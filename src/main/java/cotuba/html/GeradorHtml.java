package cotuba.html;

import cotuba.application.GeradorEbook;
import cotuba.domain.Capitulo;
import cotuba.domain.Ebook;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;

public class GeradorHtml implements GeradorEbook {

  @Override
  public void gera(final Ebook ebook) {
    final Path arquivoDeSaida = ebook.getArquivoDeSaida();

    try {
      final Path diretorioDoHTML = Files.createDirectory(arquivoDeSaida);

      int i = 1;

      for (final Capitulo capitulo : ebook.getCapitulos()) {
        final String nomeDoArquivoHTMLDoCapitulo = obtemNomeDoArquivoHTMLDoCapitulo(i, capitulo);

        final Path arquivoHTMLDoCapitulo = diretorioDoHTML.resolve(nomeDoArquivoHTMLDoCapitulo);

        final String html = """
          <!DOCTYPE html>
          <html lang="pt-BR">
            <head>
              <meta charset="UTF-8">
              <title>%s</title>
            </head>
            <body>
              %s
            </body>
          </html>
          """.formatted(capitulo.getTitulo(), capitulo.getConteudoHTML());

        Files.writeString(arquivoHTMLDoCapitulo, html, StandardCharsets.UTF_8);

        i++;
      }
    } catch (final IOException ex) {
      throw new IllegalStateException("Erro ao criar HTML: " + arquivoDeSaida.toAbsolutePath(), ex);
    }
  }

  private String obtemNomeDoArquivoHTMLDoCapitulo(final int i, final Capitulo capitulo) {
    final String nomeArquivoHTMLCapitulo = i + "-" + removeAcentos(capitulo.getTitulo().toLowerCase()).replaceAll("\\W", "") + ".html";

    return nomeArquivoHTMLCapitulo;
  }

  // Será que remover acentos é responsabilidade do GeradorHTML?
  private String removeAcentos(String texto) {
    return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
  }

}
