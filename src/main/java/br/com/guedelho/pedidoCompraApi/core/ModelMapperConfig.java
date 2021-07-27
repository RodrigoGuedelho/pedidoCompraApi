package br.com.guedelho.pedidoCompraApi.core;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.guedelho.pedidoCompraApi.models.ItemPedido;
import br.com.guedelho.pedidoCompraApi.requestModels.ItemPedidoRequest;

@Configuration
public class ModelMapperConfig {
	
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new  ModelMapper();
		
		return modelMapper;
	}
}