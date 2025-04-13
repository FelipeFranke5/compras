package dev.franke.felipe.compras.compras.api.mapper;

import dev.franke.felipe.compras.compras.api.dto.in.CompradorINDTO;
import dev.franke.felipe.compras.compras.api.dto.out.CompradorOUTDTO;
import dev.franke.felipe.compras.compras.api.model.Comprador;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CompradorMapper {

    CompradorMapper INSTANCIA = Mappers.getMapper(CompradorMapper.class);

    CompradorINDTO compradorParaCompradorINDTO(Comprador comprador);

    Comprador compradorINDTOParaComprador(CompradorINDTO compradorINDTO);

    CompradorOUTDTO compradorParaCompradorOUTDTO(Comprador comprador);

    Comprador compradorOUTDTOParaComprador(CompradorOUTDTO compradorOUTDTO);
}
