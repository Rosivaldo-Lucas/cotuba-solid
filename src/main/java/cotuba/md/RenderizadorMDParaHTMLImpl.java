package cotuba.md;

import cotuba.domain.Capitulo;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.stream.Stream;

public class RenderizadorMDParaHTMLImpl implements RenderizadorMDParaHTML {

  @Override
  public List<Capitulo> renderiza(final Path diretorioDosMD) {
    return this.obtemArquivosMD(diretorioDosMD)
            .stream()
            .map(arquivoMD -> {
              final Capitulo capitulo = new Capitulo();
              final Node document = this.parseDoMD(arquivoMD, capitulo);

              this.renderizarParaHTML(arquivoMD, capitulo, document);

              return capitulo;
            }).toList();
  }

  private List<Path> obtemArquivosMD(final Path diretorioDosMD) {
    final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.md");

    try (final Stream<Path> arquivosMD = Files.list(diretorioDosMD)) {
      return arquivosMD
              .filter(matcher::matches)
              .sorted()
              .toList();

    } catch (final IOException ex) {
      throw new IllegalStateException("Erro tentando encontrar arquivos .md em " + diretorioDosMD.toAbsolutePath(), ex);
    }
  }

  private Node parseDoMD(final Path arquivoMD, final Capitulo capitulo) {
    final Parser parser = Parser.builder().build();

    final Node document;

    try {
      document = parser.parseReader(Files.newBufferedReader(arquivoMD));

      document.accept(new AbstractVisitor() {
        @Override
        public void visit(Heading heading) {
          if (heading.getLevel() == 1) {
            // capítulo
            final String tituloDoCapitulo = ((Text) heading.getFirstChild()).getLiteral();

            capitulo.setTitulo(tituloDoCapitulo);
          } else if (heading.getLevel() == 2) {
            // seção
          } else if (heading.getLevel() == 3) {
            // título
          }
        }

      });
    } catch (final Exception ex) {
      throw new IllegalStateException("Erro ao fazer parse do arquivo " + arquivoMD, ex);
    }

    return document;
  }

  private void renderizarParaHTML(final Path arquivoMD, final Capitulo capitulo, Node document) {
    try {
      final HtmlRenderer renderer = HtmlRenderer.builder().build();
      final String html = renderer.render(document);

      capitulo.setConteudoHTML(html);
    } catch (final Exception ex) {
      throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + arquivoMD, ex);
    }
  }

}
