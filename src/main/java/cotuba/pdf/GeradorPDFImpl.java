package cotuba.pdf;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.property.AreaBreakType;
import cotuba.domain.Capitulo;
import cotuba.domain.Ebook;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GeradorPDFImpl implements GeradorPDF {

  @Override
  public void gera(final Ebook ebook) {
    final Path arquivoDeSaida = ebook.getArquivoDeSaida();

    try(
            final PdfWriter writer = new PdfWriter(Files.newOutputStream(arquivoDeSaida));
            final PdfDocument pdf = new PdfDocument(writer);
            final Document pdfDocument = new Document(pdf)
    ) {

      for (final Capitulo capitulo : ebook.getCapitulos()) {
        final String html = capitulo.getConteudoHTML();

        final List<IElement> convertToElements = HtmlConverter.convertToElements(html);

        for (final IElement element : convertToElements) {
          pdfDocument.add((IBlockElement) element);
        }

        if (!ebook.isUltimoCapitulo(capitulo)) {
          pdfDocument.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        }
      }

    } catch (final Exception ex) {
      throw new IllegalStateException("Erro ao criar arquivo PDF: " + arquivoDeSaida.toAbsolutePath(), ex);
    }
  }

}
