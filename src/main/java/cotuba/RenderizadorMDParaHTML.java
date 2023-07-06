package cotuba;

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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RenderizadorMDParaHTML {

  public List<Capitulo> renderiza(final Path diretorioDosMD) {
    final List<Capitulo> capitulos = new ArrayList<>();

    final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.md");

    try (Stream<Path> arquivosMD = Files.list(diretorioDosMD)) {
      arquivosMD
              .filter(matcher::matches)
              .sorted()
              .forEach(arquivoMD -> {
                final Capitulo capitulo = new Capitulo();

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

                try {
                  final HtmlRenderer renderer = HtmlRenderer.builder().build();
                  final String html = renderer.render(document);

                  capitulo.setConteudoHTML(html);

                  capitulos.add(capitulo);
                } catch (final Exception ex) {
                  throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + arquivoMD, ex);
                }

              });
    } catch (final IOException ex) {
      throw new IllegalStateException("Erro tentando encontrar arquivos .md em " + diretorioDosMD.toAbsolutePath(), ex);
    }

    return capitulos;
  }

}
