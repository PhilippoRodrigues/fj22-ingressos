package br.com.caelum.ingresso.validacao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import br.com.caelum.ingresso.model.Sessao;

public class GerenciadorDeSessao {
	
	private List<Sessao> sessoesDaSala;

	public GerenciadorDeSessao(List<Sessao> sessoesDaSala) {
		this.sessoesDaSala = sessoesDaSala;
	}
	
	//Obtém o horário de início de uma sessão do dia atual
	private LocalDateTime getInicioSessaoComDiaDeHoje(Sessao sessao) {
		LocalDate hoje = LocalDate.now();
		
		return sessao.getHorario().atDate(hoje);
	}
	
	//Obtém o horário de término de uma sessão do dia atual
	private LocalDateTime getTerminoSessaoComDiaDeHoje(Sessao sessao) {
		LocalDateTime inicioSessaoNova = getInicioSessaoComDiaDeHoje(sessao);
		
		return inicioSessaoNova.plus(sessao.getFilme().getDuracao());
	}
	
	//Verifica se os horários entre os filmes de sessões distintas são conflitantes  
	private boolean horarioIsConflitante(Sessao sessaoExistente, Sessao sessaoNova) {
		LocalDateTime inicioSessaoExistente = getInicioSessaoComDiaDeHoje(sessaoExistente);
		LocalDateTime terminoSessaoExistente = getTerminoSessaoComDiaDeHoje(sessaoExistente);
		LocalDateTime inicioSessaoNova= getInicioSessaoComDiaDeHoje(sessaoNova);
		LocalDateTime terminoSessaoNova= getTerminoSessaoComDiaDeHoje(sessaoNova);
		
		boolean sessaoNovaTerminaAntesDaExistente = terminoSessaoNova.isBefore(inicioSessaoExistente);
		boolean sessaoNovaComecaDepoisDaExistente = terminoSessaoExistente.isBefore(inicioSessaoNova);
		
		if(sessaoNovaTerminaAntesDaExistente || sessaoNovaComecaDepoisDaExistente) 
			return false;
		return true;
	}
	
	//Verifica se o término de uma sessão finaliza no dia seguinte
	private boolean terminaAmanha(Sessao sessao) {
		LocalDateTime terminoSessaoNova = getTerminoSessaoComDiaDeHoje(sessao);
		LocalDateTime ultimoSegundoDeHoje = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
		
		if(terminoSessaoNova.isAfter(ultimoSegundoDeHoje))
			return true;
		return false;
	}
	
	public boolean cabe(Sessao sessaoNova) {
		if(terminaAmanha(sessaoNova)) return false;
		
		return this.sessoesDaSala.stream()
				.noneMatch(sessaoExistente -> horarioIsConflitante(sessaoExistente, sessaoNova));
	}
}
