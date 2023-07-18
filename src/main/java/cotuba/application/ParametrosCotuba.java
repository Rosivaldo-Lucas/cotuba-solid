package cotuba.application;

import cotuba.domain.FormatoEbook;

import java.nio.file.Path;

public interface ParametrosCotuba {

  FormatoEbook obtemFormato();
  Path obtemDiretorioDosMD();
  Path obtemArquivoDeSaida();

}
