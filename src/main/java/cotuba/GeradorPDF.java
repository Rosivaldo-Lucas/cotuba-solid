package cotuba;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.property.AreaBreakType;
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

public class GeradorPDF {

  public void gera(final Path diretorioDosMD, final Path arquivoDeSaida) {
    try(var writer = new PdfWriter(Files.newOutputStream(arquivoDeSaida));
        final PdfDocument pdf = new PdfDocument(writer);
        final Document pdfDocument = new Document(pdf)) {

      final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.md");

      try (Stream<Path> arquivosMD = Files.list(diretorioDosMD)) {
        arquivosMD
                .filter(matcher::matches)
                .sorted()
                .forEach(arquivoMD -> {
                  final Parser parser = Parser.builder().build();

                  Node document = null;

                  try {
                    document = parser.parseReader(Files.newBufferedReader(arquivoMD));
                    document.accept(new AbstractVisitor() {
                      @Override
                      public void visit(Heading heading) {
                        if (heading.getLevel() == 1) {
                          // capítulo
                          String tituloDoCapitulo = ((Text) heading.getFirstChild()).getLiteral();
                          // TODO: usar título do capítulo
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

                    final List<IElement> convertToElements = HtmlConverter.convertToElements(html);

                    for (final IElement element : convertToElements) {
                      pdfDocument.add((IBlockElement) element);
                    }
                    // TODO: não adicionar página depois do último capítulo
                    pdfDocument.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

                  } catch (final Exception ex) {
                    throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + arquivoMD, ex);
                  }

                });
      } catch (final IOException ex) {
        throw new IllegalStateException("Erro tentando encontrar arquivos .md em " + diretorioDosMD.toAbsolutePath(), ex);
      }

    } catch (final Exception ex) {
      throw new IllegalStateException("Erro ao criar arquivo PDF: " + arquivoDeSaida.toAbsolutePath(), ex);
    }
  }

}
