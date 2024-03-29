package br.com.centralit.citcorpore.bean;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import br.com.citframework.dto.IDto;

public class SolucaoDefinitivaDTO implements IDto {


	private static final long serialVersionUID = 1L;
	
	private Integer idSolucaoDefinitiva;
	private String titulo;
	private String descricao;
	private Timestamp dataHoraCriacao;
	private String dataHoraCriacaoStr;
	private Integer idProblema;
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Timestamp getDataHoraCriacao() {
		return dataHoraCriacao;
	}
	public void setDataHoraCriacao(Timestamp dataHoraCriacao) {
		this.dataHoraCriacao = dataHoraCriacao;
		if(dataHoraCriacao != null){
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			this.dataHoraCriacaoStr = df.format(dataHoraCriacao);
		}
	}
	public String getDataHoraCriacaoStr() {
		return dataHoraCriacaoStr;
	}
	public void setDataHoraCriacaoStr(String dataHoraCriacaoStr) {
		this.dataHoraCriacaoStr = dataHoraCriacaoStr;
	}
	public Integer getIdSolucaoDefinitiva() {
		return idSolucaoDefinitiva;
	}
	public void setIdSolucaoDefinitiva(Integer idSolucaoDefinitiva) {
		this.idSolucaoDefinitiva = idSolucaoDefinitiva;
	}
	public Integer getIdProblema() {
		return idProblema;
	}
	public void setIdProblema(Integer idProblema) {
		this.idProblema = idProblema;
	}
	
	
	
}
