package br.com.centralit.citcorpore.ajaxForms;

import java.util.ArrayList;	
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import br.com.centralit.citajax.html.DocumentHTML;
import br.com.centralit.citajax.html.HTMLElement;
import br.com.centralit.citajax.html.HTMLForm;
import br.com.centralit.citajax.html.HTMLTable;
import br.com.centralit.citcorpore.bean.BaseConhecimentoDTO;
import br.com.centralit.citcorpore.bean.BaseConhecimentoRelacionadoDTO;
import br.com.centralit.citcorpore.bean.ComentariosDTO;
import br.com.centralit.citcorpore.bean.ConhecimentoProblemaDTO;
import br.com.centralit.citcorpore.bean.ContadorAcessoDTO;
import br.com.centralit.citcorpore.bean.HistoricoBaseConhecimentoDTO;
import br.com.centralit.citcorpore.bean.ItemConfiguracaoDTO;
import br.com.centralit.citcorpore.bean.PastaDTO;
import br.com.centralit.citcorpore.bean.ProblemaDTO;
import br.com.centralit.citcorpore.bean.RequisicaoLiberacaoDTO;
import br.com.centralit.citcorpore.bean.RequisicaoMudancaDTO;
import br.com.centralit.citcorpore.bean.SolicitacaoServicoDTO;
import br.com.centralit.citcorpore.bean.UsuarioDTO;
import br.com.centralit.citcorpore.integracao.ConhecimentoProblemaDao;
import br.com.centralit.citcorpore.negocio.BaseConhecimentoRelacionadoService;
import br.com.centralit.citcorpore.negocio.BaseConhecimentoService;
import br.com.centralit.citcorpore.negocio.ComentariosService;
import br.com.centralit.citcorpore.negocio.ContadorAcessoService;
import br.com.centralit.citcorpore.negocio.HistoricoBaseConhecimentoService;
import br.com.centralit.citcorpore.negocio.ItemConfiguracaoService;
import br.com.centralit.citcorpore.negocio.PastaService;
import br.com.centralit.citcorpore.negocio.PerfilAcessoPastaService;
import br.com.centralit.citcorpore.negocio.ProblemaService;
import br.com.centralit.citcorpore.negocio.RequisicaoLiberacaoService;
import br.com.centralit.citcorpore.negocio.RequisicaoMudancaService;
import br.com.centralit.citcorpore.negocio.SolicitacaoServicoService;
import br.com.centralit.citcorpore.negocio.UsuarioService;
import br.com.centralit.citcorpore.util.CITCorporeUtil;
import br.com.centralit.citcorpore.util.Enumerados.PermissaoAcessoPasta;
import br.com.centralit.citcorpore.util.UtilI18N;
import br.com.centralit.citcorpore.util.WebUtil;
import br.com.centralit.lucene.Lucene;
import br.com.citframework.service.ServiceLocator;
import br.com.citframework.util.UtilDatas;
import br.com.citframework.util.UtilStrings;

@SuppressWarnings({ "unused", "rawtypes" })
public class BaseConhecimentoView extends BaseConhecimento {

	private UsuarioDTO usuario;

	public void load(DocumentHTML document, HttpServletRequest request, HttpServletResponse response) throws Exception {
		usuario = WebUtil.getUsuario(request);
		
		this.alimentaComboPastasBaseConhecimento(document, request);

		request.getSession(true).setAttribute("colUploadsGED", null);

		document.executeScript("uploadAnexos.clear()");

		CITCorporeUtil.limparFormulario(document);
 
		// document.getForm("form").lockForm();

		this.montarArvoreDePastas(document, request, response);
		
		BaseConhecimentoDTO baseConhecimentoDto = new BaseConhecimentoDTO();

		baseConhecimentoDto = (BaseConhecimentoDTO) document.getBean();
		Integer idBaseCon = null;
		if(request.getParameter("id") != null){
		 idBaseCon = Integer.parseInt(request.getParameter("id"));
		}
		if(idBaseCon != null){
			baseConhecimentoDto.setIdBaseConhecimento(idBaseCon);
		}
		if (baseConhecimentoDto.getIdBaseConhecimento() != null){
			restore(document, request, response);
			document.getElementById("conhecimento").setVisible(true);
			document.executeScript("desabilitaDivPesquisa()");
		}
		//fechando janela aguarde, quando o request � do portal ocorre um erro se utilizar o parent.fecharJanelaAguarde()
		//	document.getJanelaPopupById("JANELA_AGUARDE_MENU").hide();
		
		
		String origemRequest = request.getParameter("origem");
		if (origemRequest != null && origemRequest.equals("portal")){
			document.getJanelaPopupById("JANELA_AGUARDE_MENU").hide();
		} else {
			document.getJanelaPopupById("JANELA_AGUARDE_MENU").hide();
			document.executeScript("parent.fecharJanelaAguarde();");
		}
		
	}

	/**
	 * Restaura Base de Conhecimento.
	 * 
	 * @param document
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author valdoilo.damasceno
	 */
	public void restore(DocumentHTML document, HttpServletRequest request, HttpServletResponse response) throws Exception {
		BaseConhecimentoDTO baseConhecimentoDto = new BaseConhecimentoDTO();

		baseConhecimentoDto = (BaseConhecimentoDTO) document.getBean();
		
		String iframe = baseConhecimentoDto.getIframe();

		PastaDTO pastaDto = new PastaDTO();

		BaseConhecimentoService baseConhecimentoService = (BaseConhecimentoService) ServiceLocator.getInstance().getService(BaseConhecimentoService.class, null);

		ComentariosService comentariosService = (ComentariosService) ServiceLocator.getInstance().getService(ComentariosService.class, null);

		PastaService pastaService = (PastaService) ServiceLocator.getInstance().getService(PastaService.class, null);

		baseConhecimentoDto = (BaseConhecimentoDTO) baseConhecimentoService.restore(baseConhecimentoDto);

		BaseConhecimentoRelacionadoService baseConhecimentoRelacionadoService = (BaseConhecimentoRelacionadoService) ServiceLocator.getInstance().getService(BaseConhecimentoRelacionadoService.class, null);

		PerfilAcessoPastaService perfilAcessoPastaService = (PerfilAcessoPastaService) ServiceLocator.getInstance().getService(PerfilAcessoPastaService.class, null);

		if (baseConhecimentoDto != null && baseConhecimentoDto.getIdPasta() != null) {

			pastaDto.setId(baseConhecimentoDto.getIdPasta());

			pastaDto = (PastaDTO) pastaService.restore(pastaDto);

			baseConhecimentoDto.setNomePasta(pastaDto.getNome());

		}

		request.getSession().removeAttribute("idBaseConhecimento");
		
		Double media = null;
		
		if (baseConhecimentoDto != null){
			request.getSession().setAttribute("idBaseConhecimento", baseConhecimentoDto.getIdBaseConhecimento());
			
			media = baseConhecimentoService.calcularNota(baseConhecimentoDto.getIdBaseConhecimento());
		}


		
		if (media != null) {
			baseConhecimentoDto.setMedia(media.toString());
		}

		Long quantidadeVotos = null;
		if (baseConhecimentoDto != null){
			
			quantidadeVotos = baseConhecimentoService.contarVotos(baseConhecimentoDto.getIdBaseConhecimento());
		}
		if (quantidadeVotos != null) {
			baseConhecimentoDto.setVotos(quantidadeVotos.toString());
		}

		super.restaurarAnexos(request, baseConhecimentoDto);

		UsuarioDTO usuarioDto = WebUtil.getUsuario(request);

		HTMLForm form = CITCorporeUtil.limparFormulario(document);
		document.executeScript("uploadAnexos.clear()");
		form.setValues(baseConhecimentoDto);
		if (media != null) {
			document.executeScript("createDiv('" + baseConhecimentoDto.getMedia() + "')");
		} else {
			document.executeScript("document.getElementById('divMostraNota').innerHTML = ''");
		}

		document.executeScript("uploadAnexos.refresh()");
		document.executeScript("deleteAllRows()");

		// document.getForm("form").lockForm();

		if (baseConhecimentoDto != null){
			document.getElementById("conteudo").setInnerHTML(baseConhecimentoDto.getConteudo());
		}
		
		String divInfoAdicional = "";
		if (baseConhecimentoDto != null && baseConhecimentoDto.getDireitoAutoral() != null && baseConhecimentoDto.getDireitoAutoral().equalsIgnoreCase("S")){
			divInfoAdicional += UtilI18N.internacionaliza(request, "baseconhecimento.existedireitoautoral") + "<br>";
		}
		if (baseConhecimentoDto != null && baseConhecimentoDto.getLegislacao() != null && baseConhecimentoDto.getLegislacao().equalsIgnoreCase("S")){
			divInfoAdicional += UtilI18N.internacionaliza(request, "baseconhecimento.existelegislacao") + "<br>";
		}
		document.getElementById("divInfoAdicional").setInnerHTML(divInfoAdicional);

		document.executeScript("executeJS()");

		document.executeScript("deleteAllRowsConhecimento()");
		
		Collection<BaseConhecimentoRelacionadoDTO> listBaseConhecimentoRelacionadoDto  = null;
		if(baseConhecimentoDto != null){
			listBaseConhecimentoRelacionadoDto = baseConhecimentoRelacionadoService.listByIdBaseConhecimento(baseConhecimentoDto.getIdBaseConhecimento());
		}

		if (listBaseConhecimentoRelacionadoDto != null && !listBaseConhecimentoRelacionadoDto.isEmpty()) {

			for (BaseConhecimentoRelacionadoDTO baseConhecimentoRelacionadoDto : listBaseConhecimentoRelacionadoDto) {

				if (baseConhecimentoRelacionadoDto.getIdBaseConhecimentoRelacionado() != null) {

					BaseConhecimentoDTO conhecimentoRelacionadoDto = new BaseConhecimentoDTO();

					conhecimentoRelacionadoDto.setIdBaseConhecimento(baseConhecimentoRelacionadoDto.getIdBaseConhecimentoRelacionado());

					conhecimentoRelacionadoDto = (BaseConhecimentoDTO) baseConhecimentoService.restore(conhecimentoRelacionadoDto);

					usuario = WebUtil.getUsuario(request);

					String titulo = "";

					PermissaoAcessoPasta permissao = null;

					pastaDto.setId(conhecimentoRelacionadoDto.getIdPasta());

					pastaDto = (PastaDTO) pastaService.restore(pastaDto);

					permissao = perfilAcessoPastaService.verificarPermissaoDeAcessoPasta(usuario, pastaDto);

					if (permissao != null) {
						if (PermissaoAcessoPasta.SEMPERMISSAO.equals(permissao)) {

							titulo = "- Confidencial";

						}

					} else {

						titulo = "- Confidencial";
					}

					document.executeScript("addLinhaTabelaConhecimentoRelacionado(" + conhecimentoRelacionadoDto.getIdBaseConhecimento() + ",'" + conhecimentoRelacionadoDto.getTitulo() + " " + titulo + "'," + true + ","
							+ conhecimentoRelacionadoDto.getIdPasta() + ");");

				}

			}
		}

		if (baseConhecimentoDto != null && (iframe == null || !iframe.equalsIgnoreCase("true"))) {

			ProblemaService problemaService = (ProblemaService) ServiceLocator.getInstance().getService(ProblemaService.class, null);
			RequisicaoMudancaService requisicaoMudancaService = (RequisicaoMudancaService) ServiceLocator.getInstance().getService(RequisicaoMudancaService.class, null);
			ItemConfiguracaoService itemConfiguracaoService = (ItemConfiguracaoService) ServiceLocator.getInstance().getService(ItemConfiguracaoService.class, null);
			SolicitacaoServicoService solicitacaoServicoService = (SolicitacaoServicoService) ServiceLocator.getInstance().getService(SolicitacaoServicoService.class, null);
			RequisicaoLiberacaoService requisicaoLiberacaoService = (RequisicaoLiberacaoService) ServiceLocator.getInstance().getService(RequisicaoLiberacaoService.class, null);

			HTMLTable tblProblema = document.getTableById("tblProblema");
			tblProblema.deleteAllRows();

			@SuppressWarnings("unchecked")
			Collection <ProblemaDTO>colProblema = problemaService.findByConhecimento(baseConhecimentoDto);

			if (colProblema != null) {
				ArrayList<ProblemaDTO> listProblema = new ArrayList<ProblemaDTO>();
				for(ProblemaDTO problema : colProblema){
					if(problema.getStatus()!= null){
						if(problema.getStatus().equalsIgnoreCase("Cancelada")){
							problema.setStatus(UtilI18N.internacionaliza(request, "citcorpore.comum.cancelada"));
						} else if(problema.getStatus().equalsIgnoreCase("Registrado")||problema.getStatus().equalsIgnoreCase("Registrada")){
							problema.setStatus(UtilI18N.internacionaliza(request, "citcorpore.comum.registrada"));
						} else if(problema.getStatus().equalsIgnoreCase("Resolvido")){
							problema.setStatus(UtilI18N.internacionaliza(request, "problema.resolvido"));
						} else if(problema.getStatus().equalsIgnoreCase("Registro de Erro Conhecido")){
							problema.setStatus(UtilI18N.internacionaliza(request, "problema.registroErroConhecido"));
						} else if(problema.getStatus().equalsIgnoreCase("Em Investiga��o")){
							problema.setStatus(UtilI18N.internacionaliza(request, "pesquisaProblema.emInvestigacao"));
						} else if(problema.getStatus().equalsIgnoreCase("Resolu��o")){
							problema.setStatus(UtilI18N.internacionaliza(request, "pesquisaProblema.resolucao"));
						}	
					}
					listProblema.add(problema);
				}
				tblProblema.addRowsByCollection(listProblema, new String[] { "", "", "titulo", "status" }, new String[] { "idProblema" }, UtilI18N.internacionaliza(request, "baseConhecimento.problemaExiste"), new String[] { "exibeIconesProblema" }, "buscaProblema", null);
				document.executeScript("HTMLUtils.applyStyleClassInAllCells('tblProblema', 'tblProblema');");
			}

			HTMLTable tblMudanca = document.getTableById("tblMudanca");
			tblMudanca.deleteAllRows();

			@SuppressWarnings("unchecked")
			Collection <RequisicaoMudancaDTO>colMudanca = requisicaoMudancaService.findByConhecimento(baseConhecimentoDto);
			if (colMudanca != null) {
				ArrayList<RequisicaoMudancaDTO> listMudancas = new ArrayList<RequisicaoMudancaDTO>();
				for(RequisicaoMudancaDTO mudanca : colMudanca){
					if(mudanca.getStatus()!= null){
						if(mudanca.getStatus().equalsIgnoreCase("Registrada")){
							mudanca.setStatus(UtilI18N.internacionaliza(request, "citcorpore.comum.registrada"));
						} else if(mudanca.getStatus().equalsIgnoreCase("Rejeitada")){
							mudanca.setStatus(UtilI18N.internacionaliza(request, "citcorpore.comum.rejeitada"));
						} else if(mudanca.getStatus().equalsIgnoreCase("Cancelada")){
							mudanca.setStatus(UtilI18N.internacionaliza(request, "citcorpore.comum.cancelada"));
						} else if(mudanca.getStatus().equalsIgnoreCase("Concluida")){
							mudanca.setStatus(UtilI18N.internacionaliza(request, "citcorpore.comum.concluida"));
						} else if(mudanca.getStatus().equalsIgnoreCase("Proposta")){
							mudanca.setStatus(UtilI18N.internacionaliza(request, "citcorpore.comum.proposta"));
						} else if(mudanca.getStatus().equalsIgnoreCase("Aprovada")){
							mudanca.setStatus(UtilI18N.internacionaliza(request, "citcorpore.comum.aprovada"));
						} else if(mudanca.getStatus().equalsIgnoreCase("Executada")){
							mudanca.setStatus(UtilI18N.internacionaliza(request, "citcorpore.comum.executada"));
						}
					}
					listMudancas.add(mudanca);
				}
					
				tblMudanca.addRowsByCollection(listMudancas, new String[] { "", "", "titulo", "status" }, new String[] { "idRequisicaoMudanca" }, UtilI18N.internacionaliza(request, "baseConhecimento.mudancaExiste"), new String[] { "exibeIconesMudanca" }, null,
						null);
				document.executeScript("HTMLUtils.applyStyleClassInAllCells('tblMudanca', 'tblMudanca');");
			}
			
			HTMLTable tblLiberacao = document.getTableById("tblLiberacao");
			tblLiberacao.deleteAllRows();
			
			Collection <RequisicaoLiberacaoDTO>colLiberacao = requisicaoLiberacaoService.findByConhecimento(baseConhecimentoDto);
			if (colLiberacao != null) {
				ArrayList<RequisicaoLiberacaoDTO> listLiberacao = new ArrayList<RequisicaoLiberacaoDTO>();
				for(RequisicaoLiberacaoDTO liberacao : colLiberacao){
					if(liberacao.getStatus()!= null){
						if(liberacao.getStatus().equalsIgnoreCase("EmExecucao")){
							liberacao.setStatus(UtilI18N.internacionaliza(request, "liberacao.emExecucao"));
						} else if(liberacao.getStatus().equalsIgnoreCase("Resolvida")){
							liberacao.setStatus(UtilI18N.internacionaliza(request, "citcorpore.comum.resolvida"));
						} else if(liberacao.getStatus().equalsIgnoreCase("Cancelada")){
							liberacao.setStatus(UtilI18N.internacionaliza(request, "liberacao.cancelada"));
						} else if(liberacao.getStatus().equalsIgnoreCase("Finalizada")){
							liberacao.setStatus(UtilI18N.internacionaliza(request, "liberacao.finalizada"));
						}
					}
					listLiberacao.add(liberacao);
				}
				tblLiberacao.addRowsByCollection(listLiberacao, new String[] { "", "", "titulo", "status" }, new String[] { "idRequisicaoLiberacao" }, UtilI18N.internacionaliza(request, "baseConhecimento.mudancaExiste"), new String[] { "exibeIconesLiberacao" }, null,
						null);
				document.executeScript("HTMLUtils.applyStyleClassInAllCells('tblLiberacao', 'tblLiberacao');");
			}

			HTMLTable tblIC = document.getTableById("tblIC");
			tblIC.deleteAllRows();

			Collection colIc = itemConfiguracaoService.findByConhecimento(baseConhecimentoDto);

			if (colIc != null) {
				tblIC.addRowsByCollection(colIc, new String[] { "", "idItemConfiguracao", "identificacao", "" }, new String[] { "idItemConfiguracao" }, UtilI18N.internacionaliza(request, "baseConhecimento.itemExiste"),
						new String[] { "exibeIconesIC" }, null, null);

				document.executeScript("HTMLUtils.applyStyleClassInAllCells('tblIC', 'tblIC');");
			}

			HTMLTable tblSolicitacao = document.getTableById("tblSolicitacao");
			tblSolicitacao.deleteAllRows();

			Collection colSolicitacao = solicitacaoServicoService.findByConhecimento(baseConhecimentoDto);

			if (colSolicitacao != null) {
				tblSolicitacao.addRowsByCollection(colSolicitacao, new String[] { "", "", "idSolicitacaoServico", "nomeServico" }, new String[] { "idSolicitacaoServico" }, UtilI18N.internacionaliza(request, "baseConhecimento.solicitacaoExiste"),
						new String[] { "exibeIconesSolicitacao" }, null, null);

				document.executeScript("HTMLUtils.applyStyleClassInAllCells('tblSolicitacao', 'tblSolicitacao');");
			}

		}
	}

	/**
	 * Busca somente pastas com perfil de acesso onde usuario logado tenha acesso.
	 * 
	 * @param usuario
	 * @param idgrupo
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void montarArvoreDePastas(DocumentHTML document, HttpServletRequest request, HttpServletResponse response) throws Exception {

		BaseConhecimentoService baseConhecimentoService = (BaseConhecimentoService) ServiceLocator.getInstance().getService(BaseConhecimentoService.class, null);

		PastaService pastaService = (PastaService) ServiceLocator.getInstance().getService(PastaService.class, null);

		PerfilAcessoPastaService perfilAcessoPastaService = (PerfilAcessoPastaService) ServiceLocator.getInstance().getService(PerfilAcessoPastaService.class, null);

		UsuarioDTO usuarioDto = WebUtil.getUsuario(request);

		Collection<PastaDTO> listPastas = pastaService.listPastaSuperiorSemPai();

		StringBuffer sb = new StringBuffer();

		sb.append("<div id=\"corpoInf\">");
		sb.append("<br>");
		sb.append("<ul id=\"browser\" class=\"filetree treeview\">");

		if (listPastas != null) {

			for (PastaDTO pasta : listPastas) {

				String nomePasta = pasta.getNome();

				Integer idPasta = pasta.getId();

				sb.append("<li>");
				sb.append("<div class=\"hitarea closed-hitarea collapsable-hitarea\">");
				sb.append("</div>");
				sb.append("<span class=\"folder\">");

				sb.append(super.obterStatusPermissao(request, pasta, nomePasta, usuarioDto));

				sb.append("</span>");
				sb.append("<ul> ");

				BaseConhecimentoDTO baseConhecimento = new BaseConhecimentoDTO();

				Collection<BaseConhecimentoDTO> listBaseconhecimento = baseConhecimentoService.listarBaseConhecimentoByPasta(pasta);

				if (listBaseconhecimento != null && !listBaseconhecimento.isEmpty()) {

					sb.append("<ul>");

					for (BaseConhecimentoDTO base : listBaseconhecimento) {

						sb.append("<li>");

						sb.append("<span class=\"file\">");

						sb.append("<a  href='#'  onclick='incidentesAbertosPorBaseConhecimnto(" + base.getIdBaseConhecimento() + ");itensConfiguracoesAbertosPorBaseConhecimnto(" + base.getIdBaseConhecimento()
								+ ");problemasAbertosPorBaseConhecimnto(" + base.getIdBaseConhecimento() + ");mudancasAbertasPorBaseConhecimnto(" + base.getIdBaseConhecimento()
								+ ");contadorClicks(" + base.getIdBaseConhecimento() + ");comentariosAbertosPorBaseConhecimnto(" + base.getIdBaseConhecimento() + ");verificarPermissaoDeAcesso(" + base.getIdPasta() + "," + base.getIdBaseConhecimento()
								+ ");desabilitaDivPesquisa()'    id='idTitulo" + base.getIdBaseConhecimento() + "' >");

						// sb.append(super.obterStatusConfidencial(pasta, base.getTitulo(), usuarioDto));

						if (base.getPrivacidade() != null && !StringUtils.isEmpty(base.getPrivacidade())) {
							sb.append(" " + base.getTitulo() + " - " + super.getPrivacidade(request, base.getPrivacidade()) + " ");
						} else {
							sb.append(" " + base.getTitulo() + " ");
						}

						sb.append("</a>");

						sb.append("</span>");

						sb.append("</li>");

					}

					sb.append("</ul>");
				}

				Collection<PastaDTO> listaDeSubPastas = pastaService.listSubPastas(pasta);

				if (listaDeSubPastas != null) {

					this.gerarSubpastas(sb, listaDeSubPastas, pasta, request);
				}

				sb.append("</li>");
				sb.append("</ul>");
				sb.append("</li>");
			}
		}
		sb.append("</ul>");
		sb.append("</div>");

		HTMLElement divPrincipal = document.getElementById("principalBaseConhecimento");
		divPrincipal.setInnerHTML(sb.toString());
		document.executeScript("tree(\"#browser\");");
	}

	public void verificarPermissaoDeAcesso(DocumentHTML document, HttpServletRequest request, HttpServletResponse response) throws Exception {

		UsuarioDTO usuarioDto = WebUtil.getUsuario(request);

		BaseConhecimentoDTO baseConhecimentoDto = new BaseConhecimentoDTO();

		baseConhecimentoDto = (BaseConhecimentoDTO) document.getBean();

		PastaService pastaService = (PastaService) ServiceLocator.getInstance().getService(PastaService.class, null);

		PerfilAcessoPastaService perfilAcessoPastaService = (PerfilAcessoPastaService) ServiceLocator.getInstance().getService(PerfilAcessoPastaService.class, null);

		BaseConhecimentoService baseConhecimentoService = (BaseConhecimentoService) ServiceLocator.getInstance().getService(BaseConhecimentoService.class, null);

		if (baseConhecimentoDto.getIdPasta() == null) {
			if (baseConhecimentoDto.getIdBaseConhecimento() != null) {

				baseConhecimentoDto = (BaseConhecimentoDTO) baseConhecimentoService.restore(baseConhecimentoDto);
			}
		}

		PastaDTO pastaDto = new PastaDTO();

		if (baseConhecimentoDto.getIdPasta() != null) {

			pastaDto.setId(baseConhecimentoDto.getIdPasta());

			pastaDto = (PastaDTO) pastaService.restore(pastaDto);
		}

		PermissaoAcessoPasta permissao = null;

		permissao = perfilAcessoPastaService.verificarPermissaoDeAcessoPasta(usuarioDto, pastaDto);

		if (permissao != null) {

			if (PermissaoAcessoPasta.SEMPERMISSAO.equals(permissao)) {

				document.alert("Usu�rio sem permiss�o de acesso.");
				document.executeScript("$('#conhecimento').hide();");
				return;

			} else {

				if (PermissaoAcessoPasta.LEITURA.equals(permissao)) {

					document.executeScript("fecharPesquisa();");

					document.executeScript("corTitulo(" + baseConhecimentoDto.getIdBaseConhecimento() + ");");

					document.executeScript("tituloBaseConhecimentoView(" + baseConhecimentoDto.getIdBaseConhecimento() + ")");
					
					document.executeScript("contadorClicks(" + baseConhecimentoDto.getIdBaseConhecimento() + ");");

				} else {

					if (PermissaoAcessoPasta.LEITURAGRAVACAO.equals(permissao)) {

						document.executeScript("fecharPesquisa();");

						document.executeScript("corTitulo(" + baseConhecimentoDto.getIdBaseConhecimento() + ");");

						document.executeScript("tituloBaseConhecimentoView(" + baseConhecimentoDto.getIdBaseConhecimento() + ")");
						
						document.executeScript("contadorClicks(" + baseConhecimentoDto.getIdBaseConhecimento() + ");");

					}
				}

			}

		} else {
			document.executeScript("$('#conhecimento').hide();");
			document.alert(UtilI18N.internacionaliza(request, "baseConhecimento.usuarioSemPermissao"));
			return;
		}

	}

	public void pesquisaBaseConhecimento(DocumentHTML document, HttpServletRequest request, HttpServletResponse response) throws Exception {

		BaseConhecimentoDTO baseConhecimentoDto = new BaseConhecimentoDTO();

		usuario = WebUtil.getUsuario(request);

		baseConhecimentoDto = (BaseConhecimentoDTO) document.getBean();
		
		BaseConhecimentoService baseConhecimentoService = (BaseConhecimentoService) ServiceLocator.getInstance().getService(BaseConhecimentoService.class, null);

		List<BaseConhecimentoDTO> listaPesquisaBaseConhecimento = null;

		if (baseConhecimentoDto.getTermoPesquisa() == null || baseConhecimentoDto.getTermoPesquisa().trim().equalsIgnoreCase("") && baseConhecimentoDto.getIdUsuarioAutorPesquisa() == null
				&& baseConhecimentoDto.getIdUsuarioAprovadorPesquisa() == null && baseConhecimentoDto.getDataInicioPesquisa() == null && baseConhecimentoDto.getDataPublicacaoPesquisa() == null
				&& baseConhecimentoDto.getTermoPesquisaNota().equalsIgnoreCase("")) {

			document.alert(UtilI18N.internacionaliza(request, "baseConhecimento.validacao.informetermo"));

			return;

		} else {
				document.executeScript("$('#resultPesquisaPai').dialog('open');");
				Lucene lucene = new Lucene();
				listaPesquisaBaseConhecimento = lucene.pesquisaBaseConhecimento(baseConhecimentoDto);
		}

		StringBuilder TabelaDados = new StringBuilder("<table>");

		CompararBaseConhecimentoGrauImportancia comparaGrauDeImportancia = new CompararBaseConhecimentoGrauImportancia();

		if (listaPesquisaBaseConhecimento != null && !listaPesquisaBaseConhecimento.isEmpty()) {

			for (BaseConhecimentoDTO baseConhecimentoDTO : listaPesquisaBaseConhecimento) {
				if (baseConhecimentoDTO != null && baseConhecimentoDTO.getIdPasta() != null) {
						if (baseConhecimentoDTO != null) {
							if (baseConhecimentoService.obterGrauDeImportanciaParaUsuario(baseConhecimentoDTO, usuario) != null) {
								Integer grauImportancia = baseConhecimentoService.obterGrauDeImportanciaParaUsuario(baseConhecimentoDTO, usuario);
								baseConhecimentoDTO.setGrauImportancia(grauImportancia);
							}
						}
				}
			}

			Collections.sort(listaPesquisaBaseConhecimento, comparaGrauDeImportancia);

			for (BaseConhecimentoDTO dto : listaPesquisaBaseConhecimento) {

				Integer importancia = dto.getGrauImportancia();

				String titulo = UtilStrings.nullToVazio(dto.getTitulo());

				titulo = titulo.replaceAll("\"", "");
				titulo = titulo.replaceAll("/", "");

				TabelaDados.append("<ul>");
				TabelaDados.append("<tr style='height: 25px !important;'>");
				TabelaDados.append("<td style='FONT-WEIGHT: bold; FONT-SIZE: small; FONT-FAMILY: Arial; width: 422px;'>");
				TabelaDados.append("<li>");
				TabelaDados.append("<div>");
				TabelaDados.append("<a href='#' class='ui-icon-bullet' onclick='verificarPermissaoDeAcesso(" + dto.getIdPasta() + "," + dto.getIdBaseConhecimento() + ");contadorClicks(" + dto.getIdBaseConhecimento());
					TabelaDados.append(");incidentesAbertosPorBaseConhecimnto(" + dto.getIdBaseConhecimento() + ");problemasAbertosPorBaseConhecimnto(" + dto.getIdBaseConhecimento());
					TabelaDados.append(");itensConfiguracoesAbertosPorBaseConhecimnto(" + dto.getIdBaseConhecimento() + ");mudancasAbertasPorBaseConhecimnto(" + dto.getIdBaseConhecimento());
					TabelaDados.append(");comentariosAbertosPorBaseConhecimnto(" + dto.getIdBaseConhecimento() + ");desabilitaDivPesquisa()' id='idTitulo" + dto.getIdBaseConhecimento() + "' >");
				TabelaDados.append(titulo + super.getGrauImportancia(request, importancia) + "</a>");
				TabelaDados.append("</div>");
				TabelaDados.append("</td>");
				TabelaDados.append("</li>");
				TabelaDados.append("</tr>");
				TabelaDados.append("</ul>");
			}

		} else {
			TabelaDados.append("<tr style='height: 25px !important;'>");
			TabelaDados.append("<td style='FONT-WEIGHT: bold; FONT-SIZE: small; FONT-FAMILY: 'Arial'; width : 422px;'>");
			TabelaDados.append("<label> " + UtilI18N.internacionaliza(request, "citcorpore.comum.resultado") + "</label>");
			TabelaDados.append("</td>");
			TabelaDados.append("</tr>");
		}
	
		TabelaDados.append("</table>");

		document.getElementById("resultPesquisa").setInnerHTML(TabelaDados.toString());

		document.getElementById("resultPesquisaPai").setVisible(true);
		
	}

	public void contadorDeClicks(DocumentHTML document, HttpServletRequest request, HttpServletResponse response) throws Exception {
		usuario = WebUtil.getUsuario(request);

		BaseConhecimentoDTO baseConhecimentoDto = new BaseConhecimentoDTO();

		baseConhecimentoDto = (BaseConhecimentoDTO) document.getBean();

		ContadorAcessoDTO contadorAcessoDto = new ContadorAcessoDTO();
		ContadorAcessoService contadorAcessoService = (ContadorAcessoService) ServiceLocator.getInstance().getService(ContadorAcessoService.class, null);
		if (contadorAcessoDto.getIdContadorAcesso() == null) {
			contadorAcessoDto.setIdUsuario(usuario.getIdUsuario());
			contadorAcessoDto.setIdBaseConhecimento(baseConhecimentoDto.getIdBaseConhecimento());
			contadorAcessoDto.setDataHoraAcesso(UtilDatas.getDataHoraAtual());
			contadorAcessoDto.setContadorAcesso(1);
			if (contadorAcessoService.verificarDataHoraDoContadorDeAcesso(contadorAcessoDto)) {
				contadorAcessoService.create(contadorAcessoDto);
				
				// Avalia��o - M�dia da nota dada pelos usu�rios
				BaseConhecimentoService baseConhecimentoService = (BaseConhecimentoService) ServiceLocator.getInstance().getService(BaseConhecimentoService.class, null);
				baseConhecimentoDto = (BaseConhecimentoDTO) baseConhecimentoService.restore(baseConhecimentoDto);
				
				Double media = baseConhecimentoService.calcularNota(baseConhecimentoDto.getIdBaseConhecimento());
				if (media != null)
					baseConhecimentoDto.setMedia(media.toString());
				else
					baseConhecimentoDto.setMedia(null);
				contadorAcessoService = (ContadorAcessoService) ServiceLocator.getInstance().getService(ContadorAcessoService.class, null);

				// Qtde de cliques
				Integer quantidadeDeCliques = contadorAcessoService.quantidadesDeAcessoPorBaseConhecimnto(baseConhecimentoDto);
				if (quantidadeDeCliques != null)
					baseConhecimentoDto.setContadorCliques(quantidadeDeCliques);
				else
					baseConhecimentoDto.setContadorCliques(0);

				Lucene lucene = new Lucene();
				lucene.indexarBaseConhecimento(baseConhecimentoDto);
			}
		}

	}

	public void mostrarQuantidadeDeIncidentesAbertosPorBaseConhecimento(DocumentHTML document, HttpServletRequest request, HttpServletResponse response) throws Exception {

		BaseConhecimentoDTO baseConhecimentoDto = new BaseConhecimentoDTO();

		StringBuffer html = new StringBuffer();

		baseConhecimentoDto = (BaseConhecimentoDTO) document.getBean();

		SolicitacaoServicoDTO solicitacaoServicoDto = new SolicitacaoServicoDTO();

		SolicitacaoServicoService solicitacaoServicoService = (SolicitacaoServicoService) ServiceLocator.getInstance().getService(SolicitacaoServicoService.class, WebUtil.getUsuarioSistema(request));
		solicitacaoServicoDto.setIdBaseConhecimento(baseConhecimentoDto.getIdBaseConhecimento());

		Collection<SolicitacaoServicoDTO> listaquantidadeSolicitacaoPorBaseConnhecimento = solicitacaoServicoService.quantidadeSolicitacaoPorBaseConhecimento(solicitacaoServicoDto);
		if (listaquantidadeSolicitacaoPorBaseConnhecimento != null) {
			for (SolicitacaoServicoDTO solicitacao : listaquantidadeSolicitacaoPorBaseConnhecimento) {
				solicitacaoServicoDto.setIdBaseConhecimento(solicitacao.getIdBaseConhecimento());
				solicitacaoServicoDto.setQuantidade(solicitacao.getQuantidade());
			}
		}
		html.append("<div class='col_100'>");
		html.append("<fieldset  style='margin-bottom: -1px; padding-bottom: 21px;height: 34px'>");
		html.append("<div style='padding-top: 20px'>");
		html.append("<a title='"+UtilI18N.internacionaliza(request, "baseConhecimentoView.consultarSolicitacaoIncidente")+"' style='color: black !important; padding: 18px;' onclick='consultarSolicitacao(" + solicitacaoServicoDto.getIdBaseConhecimento() + ")'  href='#' class='light'>");
		html.append("<img src='" + br.com.citframework.util.Constantes.getValue("SERVER_ADDRESS") + br.com.citframework.util.Constantes.getValue("CONTEXTO_APLICACAO") + "");
		html.append("/imagens/list.png' border='0' title='"+UtilI18N.internacionaliza(request, "baseConhecimentoView.consultarSolicitacaoIncidente")+"' onclick='consultarSolicitacao(" + solicitacaoServicoDto.getIdBaseConhecimento() + ")' style='cursor:pointer'/>");
		if (solicitacaoServicoDto.getQuantidade() != null) {
			html.append("<span style='color: black !important; font-weight:bold '>"+UtilI18N.internacionaliza(request, "pesquisaBaseConhecimento.quantidadeIncidentesServicos")+": " + solicitacaoServicoDto.getQuantidade() + "</span>");
		} else {
			html.append("<span style='color: black !important; font-weight:bold '>"+UtilI18N.internacionaliza(request, "pesquisaBaseConhecimento.quantidadeIncidentesServicos")+":  </span>");
		}
		html.append("</a>");
		html.append("</div>");
		html.append("</fieldset>");
		html.append("</div>");

		document.getElementById("quantidadeSolicitacaoPorBaseConhecimento").setInnerHTML(html.toString());

	}

	public void mostrarQuantidadeDeMudancasAbertasPorBaseConhecimento(DocumentHTML document, HttpServletRequest request, HttpServletResponse response) throws Exception {

		BaseConhecimentoDTO baseConhecimentoDto = new BaseConhecimentoDTO();

		StringBuffer html = new StringBuffer();

		baseConhecimentoDto = (BaseConhecimentoDTO) document.getBean();

		RequisicaoMudancaDTO mudancaDTO = new RequisicaoMudancaDTO();

		RequisicaoMudancaService mudancaService = (RequisicaoMudancaService) ServiceLocator.getInstance().getService(RequisicaoMudancaService.class, WebUtil.getUsuarioSistema(request));

		mudancaDTO.setIdBaseConhecimento(baseConhecimentoDto.getIdBaseConhecimento());

		Collection<RequisicaoMudancaDTO> listaQuantidadeMudancasPorBaseConnhecimento = mudancaService.quantidadeMudancaPorBaseConhecimento(mudancaDTO);
		if (listaQuantidadeMudancasPorBaseConnhecimento != null) {
			for (RequisicaoMudancaDTO mudanca : listaQuantidadeMudancasPorBaseConnhecimento) {
				mudancaDTO.setIdBaseConhecimento(mudanca.getIdBaseConhecimento());
				mudancaDTO.setQuantidade(mudanca.getQuantidade());
			}
		}
		html.append("<div class='col_100'>");
		html.append("<fieldset  style='margin-bottom: -1px; padding-bottom: 21px;height: 34px'>");
		html.append("<div style='padding-top: 20px'>");
		html.append("<a title='"+UtilI18N.internacionaliza(request, "baseConhecimentoView.consultarMudancas")+"' style='color: black !important; padding: 18px;' onclick='consultarMudancas(" + mudancaDTO.getIdBaseConhecimento() + ")'  href='#' class='light'>");
		html.append("<img src='" + br.com.citframework.util.Constantes.getValue("SERVER_ADDRESS") + br.com.citframework.util.Constantes.getValue("CONTEXTO_APLICACAO") + "");
		html.append("/imagens/list.png' border='0' title='"+UtilI18N.internacionaliza(request, "baseConhecimentoView.consultarMudancas")+"' onclick='consultarMudancas(" + mudancaDTO.getIdBaseConhecimento() + ")' style='cursor:pointer'/>");
		if (mudancaDTO.getQuantidade() != null) {
			html.append("<span style='color: black !important; font-weight:bold '>"+UtilI18N.internacionaliza(request, "pesquisaBaseConhecimento.quantidadeMudancas")+": " + mudancaDTO.getQuantidade() + "</span>");
		} else {
			html.append("<span style='color: black !important; font-weight:bold '>"+UtilI18N.internacionaliza(request, "pesquisaBaseConhecimento.quantidadeMudancas")+":  </span>");
		}
		html.append("</a>");
		html.append("</div>");
		html.append("</fieldset>");
		html.append("</div>");

		document.getElementById("quantidadeMudancaPorBaseConhecimento").setInnerHTML(html.toString());

	}

	public void mostrarQuantidadeDeProblemasAbertosPorBaseConhecimento(DocumentHTML document, HttpServletRequest request, HttpServletResponse response) throws Exception {

		BaseConhecimentoDTO baseConhecimentoDto = new BaseConhecimentoDTO();

		StringBuffer html = new StringBuffer();

		baseConhecimentoDto = (BaseConhecimentoDTO) document.getBean();

		ProblemaDTO problemaDTO = new ProblemaDTO();

		ProblemaService problemaService = (ProblemaService) ServiceLocator.getInstance().getService(ProblemaService.class, WebUtil.getUsuarioSistema(request));

		problemaDTO.setIdBaseConhecimento(baseConhecimentoDto.getIdBaseConhecimento());

		Collection<ProblemaDTO> listaQuantidadeProblemasPorBaseConnhecimento = problemaService.quantidadeProblemaPorBaseConhecimento(problemaDTO);
		if (listaQuantidadeProblemasPorBaseConnhecimento != null) {
			for (ProblemaDTO problema : listaQuantidadeProblemasPorBaseConnhecimento) {
				problemaDTO.setIdBaseConhecimento(problema.getIdBaseConhecimento());
				problemaDTO.setQuantidade(problema.getQuantidade());
			}
		}
		html.append("<div class='col_100'>");
		html.append("<fieldset  style='margin-bottom: -1px; padding-bottom: 21px;height: 34px'>");
		html.append("<div style='padding-top: 20px'>");
		html.append("<a title='"+UtilI18N.internacionaliza(request, "baseConhecimentoView.consultarProblemas")+"' style='color: black !important; padding: 18px;' onclick='consultarProblemas(" + problemaDTO.getIdBaseConhecimento() + ")'  href='#' class='light'>");
		html.append("<img src='" + br.com.citframework.util.Constantes.getValue("SERVER_ADDRESS") + br.com.citframework.util.Constantes.getValue("CONTEXTO_APLICACAO") + "");
		html.append("/imagens/list.png' border='0' title='"+UtilI18N.internacionaliza(request, "baseConhecimentoView.consultarProblemas")+"' onclick='consultarProblemas(" + problemaDTO.getIdBaseConhecimento() + ")' style='cursor:pointer'/>");
		if (problemaDTO.getQuantidade() != null) {
			html.append("<span style='color: black !important; font-weight:bold '>"+UtilI18N.internacionaliza(request, "pesquisaBaseConhecimento.quantidadeProblemas")+": " + problemaDTO.getQuantidade() + "</span>");
		} else {
			html.append("<span style='color: black !important; font-weight:bold '>"+UtilI18N.internacionaliza(request, "pesquisaBaseConhecimento.quantidadeProblemas")+":  </span>");
		}
		html.append("</a>");
		html.append("</div>");
		html.append("</fieldset>");
		html.append("</div>");

		document.getElementById("quantidadeProblemaPorBaseConhecimento").setInnerHTML(html.toString());

	}

	public void mostrarQuantidadeDeComentariosAbertosPorBaseConhecimento(DocumentHTML document, HttpServletRequest request, HttpServletResponse response) throws Exception {

		BaseConhecimentoDTO baseConhecimentoDto = new BaseConhecimentoDTO();

		StringBuffer html = new StringBuffer();

		ComentariosDTO comentarioDTO = new ComentariosDTO();

		baseConhecimentoDto = (BaseConhecimentoDTO) document.getBean();

		ComentariosService comentariosService = (ComentariosService) ServiceLocator.getInstance().getService(ComentariosService.class, null);

		Collection<ComentariosDTO> comentarios = comentariosService.consultarComentarios(baseConhecimentoDto);

		if (comentarios != null && !comentarios.isEmpty()) {
			for (ComentariosDTO comentario : comentarios) {
				comentarioDTO.setIdBaseConhecimento(comentario.getIdBaseConhecimento());
			}
		}
		html.append("<div class='col_100'>");
		html.append("<fieldset  style='margin-bottom: -1px; padding-bottom: 21px;height: 34px'>");
		html.append("<div style='padding-top: 20px'>");
		html.append("<a title='"+UtilI18N.internacionaliza(request, "baseConhecimentoView.consultarComentarios")+"' style='color: black !important; padding: 18px;' onclick='consultarComentarios(" + comentarioDTO.getIdBaseConhecimento() + ")'  href='#' class='light'>");
		html.append("<img src='" + br.com.citframework.util.Constantes.getValue("SERVER_ADDRESS") + br.com.citframework.util.Constantes.getValue("CONTEXTO_APLICACAO") + "");
		html.append("/imagens/list.png' border='0' title='"+UtilI18N.internacionaliza(request, "baseConhecimentoView.consultarComentarios")+"' onclick='consultarComentarios(" + comentarioDTO.getIdBaseConhecimento() + ")' style='cursor:pointer'/>");
		if (comentarios != null && !comentarios.isEmpty()) {
			html.append("<span style='color: black !important; font-weight:bold '>"+UtilI18N.internacionaliza(request, "pesquisaBaseConhecimento.quantidadeComentarios")+":" + comentarios.size() + "</span>");
		} else {
			html.append("<span style='color: black !important; font-weight:bold '>"+UtilI18N.internacionaliza(request, "pesquisaBaseConhecimento.quantidadeComentarios")+":</span>");
		}
		html.append("</a>");
		html.append("</div>");
		html.append("</fieldset>");
		html.append("</div>");

		document.getElementById("quantidadeComentarioPorBaseConhecimento").setInnerHTML(html.toString());

	}

	public void mostrarQuantidadeDeItensConfiguracoesAbertosPorBaseConhecimento(DocumentHTML document, HttpServletRequest request, HttpServletResponse response) throws Exception {

		BaseConhecimentoDTO baseConhecimentoDto = new BaseConhecimentoDTO();

		StringBuffer html = new StringBuffer();

		baseConhecimentoDto = (BaseConhecimentoDTO) document.getBean();

		ItemConfiguracaoDTO itemConfiguracaoDTO = new ItemConfiguracaoDTO();

		ItemConfiguracaoService itemConfiguracaoService = (ItemConfiguracaoService) ServiceLocator.getInstance().getService(ItemConfiguracaoService.class, WebUtil.getUsuarioSistema(request));

		itemConfiguracaoDTO.setIdBaseConhecimento(baseConhecimentoDto.getIdBaseConhecimento());

		Collection<ItemConfiguracaoDTO> listaQuantidadeItensConfiguracoesPorBaseConnhecimento = itemConfiguracaoService.quantidadeItemConfiguracaoPorBaseConhecimento(itemConfiguracaoDTO);
		if (listaQuantidadeItensConfiguracoesPorBaseConnhecimento != null) {
			for (ItemConfiguracaoDTO itemConfiguracao : listaQuantidadeItensConfiguracoesPorBaseConnhecimento) {
				itemConfiguracaoDTO.setIdBaseConhecimento(itemConfiguracao.getIdBaseConhecimento());
				itemConfiguracaoDTO.setQuantidade(itemConfiguracao.getQuantidade());
			}
		}
		html.append("<div class='col_100'>");
		html.append("<fieldset  style='margin-bottom: -1px; padding-bottom: 21px;height: 34px'>");
		html.append("<div style='padding-top: 20px'>");
		html.append("<a title='"+UtilI18N.internacionaliza(request, "baseConhecimentoView.consultarProblemas")+"' style='color: black !important; padding: 18px;' onclick='consultarItensConfiguracoes(" + itemConfiguracaoDTO.getIdBaseConhecimento() + ")'  href='#' class='light'>");
		html.append("<img src='" + br.com.citframework.util.Constantes.getValue("SERVER_ADDRESS") + br.com.citframework.util.Constantes.getValue("CONTEXTO_APLICACAO") + "");
		html.append("/imagens/list.png' border='0' title='"+UtilI18N.internacionaliza(request, "baseConhecimentoView.consultarProblemas")+"' onclick='consultarItensConfiguracoes(" + itemConfiguracaoDTO.getIdBaseConhecimento() + ")' style='cursor:pointer'/>");
		if (itemConfiguracaoDTO.getQuantidade() != null) {
			html.append("<span style='color: black !important; font-weight:bold '>"+UtilI18N.internacionaliza(request, "pesquisaBaseConhecimento.quantidadeItensConfiguracoes")+" :" + itemConfiguracaoDTO.getQuantidade() + "</span>");
		} else {
			html.append("<span style='color: black !important; font-weight:bold '>"+UtilI18N.internacionaliza(request, "pesquisaBaseConhecimento.quantidadeItensConfiguracoes")+" :</span>");
		}
		html.append("</a>");
		html.append("</div>");
		html.append("</fieldset>");
		html.append("</div>");

		document.getElementById("quantidadeItemConfiguracaoPorBaseConhecimento").setInnerHTML(html.toString());

	}

	public void listarSolicitacaoServico(DocumentHTML document, HttpServletRequest request, HttpServletResponse response) throws Exception {

		BaseConhecimentoDTO baseConhecimentoDto = new BaseConhecimentoDTO();

		baseConhecimentoDto = (BaseConhecimentoDTO) document.getBean();

		StringBuffer html = new StringBuffer();

		SolicitacaoServicoDTO solicitacaoServicoDto = new SolicitacaoServicoDTO();

		SolicitacaoServicoService solicitacaoServicoService = (SolicitacaoServicoService) ServiceLocator.getInstance().getService(SolicitacaoServicoService.class, WebUtil.getUsuarioSistema(request));

		solicitacaoServicoDto.setIdBaseConhecimento(baseConhecimentoDto.getIdBaseConhecimento());

		Collection<SolicitacaoServicoDTO> listaSolicitacaoPorBaseConhecimento = solicitacaoServicoService.listaSolicitacaoPorBaseConhecimento(solicitacaoServicoDto);

		if (listaSolicitacaoPorBaseConhecimento != null) {
			html.append("<div>");
			html.append("<table class='table' id='tbRetorno' width='100%' >");
			html.append("<tr>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "citcorpore.comum.numero")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "solicitacaoServico.solicitante")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "citcorpore.comum.tipo")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "solicitacaoServico.datahoraabertura")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "gerenciaservico.sla")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "solicitacaoServico.descricao")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "solicitacaoServico.solucaoResposta")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "solicitacaoServico.situacao")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "solicitacaoServico.datahoralimite")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "unidade.grupo")+"");
			html.append("</th>");
			html.append("</tr>");
			for (SolicitacaoServicoDTO solicitacao : listaSolicitacaoPorBaseConhecimento) {
				html.append("<tr>");
				html.append("<td>");
				html.append("" + solicitacao.getIdSolicitacaoServico() + "");
				html.append("</td>");
				html.append("<td>");
				html.append("" + solicitacao.getNomeSolicitante() + "");
				html.append("</td>");
				html.append("<td>");
				html.append("" + solicitacao.getNomeTipoDemandaServico() + "");
				html.append("</td>");
				if (solicitacao.getSeqReabertura() == null || solicitacao.getSeqReabertura().intValue() == 0) {
					html.append("<td id='dataHoraSolicitacao'>" + UtilDatas.formatTimestamp(solicitacao.getDataHoraSolicitacao()) + "</td>");
				} else {
					html.append("<td id='dataHoraSolicitacao'>" + UtilDatas.formatTimestamp(solicitacao.getDataHoraSolicitacao()) + "<br><br>Seq.reabertura: <span style='color:red'><b>");
					html.append(+solicitacao.getSeqReabertura() + "</b></span></td>");
				}
				html.append("<td>");
				html.append("" + solicitacao.getPrazoHH() + ":" + solicitacao.getPrazoMM() + "");
				html.append("</td>");
				html.append("<td>");
				html.append("" + UtilStrings.nullToVazio(solicitacao.getDescricao()) + "");
				html.append("</td>");
				html.append("<td>");
				html.append("" + UtilStrings.nullToVazio(solicitacao.getResposta()) + "");
				html.append("</td>");
				html.append("<td>");
				html.append("" + UtilStrings.nullToVazio(solicitacao.getSituacao()) + "");
				html.append("</td>");
				html.append("<td>");
				if (solicitacao.getDataHoraLimite() != null)
				    html.append("" + UtilDatas.formatTimestamp(solicitacao.getDataHoraLimite()) + "");
				else
				    html.append("nbsp;");
				html.append("</td>");
				html.append("<td>");
				html.append("" + UtilStrings.nullToVazio(solicitacao.getSiglaGrupo()) + "");
				html.append("</td>");
				html.append("</tr>");
			}
			html.append("</table>");
			html.append("</div>");
			document.getElementById("dadosSolicitacao/Incidetes").setInnerHTML(html.toString());
			document.executeScript("$('#POPUP_DADOSSOLICITCAO').dialog('open')");
		} else {
			document.alert(UtilI18N.internacionaliza(request, "MSG04"));
		}
	}

	public void listarProblema(DocumentHTML document, HttpServletRequest request, HttpServletResponse response) throws Exception {

		BaseConhecimentoDTO baseConhecimentoDto = new BaseConhecimentoDTO();

		baseConhecimentoDto = (BaseConhecimentoDTO) document.getBean();

		StringBuffer html = new StringBuffer();

		ProblemaDTO problemaDTO = new ProblemaDTO();

		ProblemaService problemaService = (ProblemaService) ServiceLocator.getInstance().getService(ProblemaService.class, WebUtil.getUsuarioSistema(request));

		problemaDTO.setIdBaseConhecimento(baseConhecimentoDto.getIdBaseConhecimento());

		Collection<ProblemaDTO> listaProblemaPorBaseConhecimento = problemaService.listaProblemaPorBaseConhecimento(problemaDTO);

		if (listaProblemaPorBaseConhecimento != null) {
			html.append("<div>");
			html.append("<table class='table' id='tbRetorno' width='100%' >");
			html.append("<tr>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "citcorpore.comum.numero")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "problema.proprietario")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "problema.titulo")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "solicitacaoServico.datahoraabertura")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "solicitacaoServico.descricao")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "problema.causa_raiz")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "solicitacaoServico.solucao")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "requisicaMudanca.status")+"");
			html.append("</th>");
			html.append("</tr>");
			for (ProblemaDTO problema : listaProblemaPorBaseConhecimento) {
				html.append("<tr>");
				html.append("<td>");
				html.append("" + problema.getIdProblema() + "");
				html.append("</td>");
				html.append("<td>");
				html.append("" + problema.getNomeProprietario() + "");
				html.append("</td>");
				html.append("<td>");
				html.append("" + problema.getTitulo() + "");
				html.append("</td>");
				html.append("<td>");
				if (problema.getDataHoraInicio() == null) {
					html.append("");
				} else {
					html.append("" + UtilDatas.formatTimestamp(problema.getDataHoraInicio()) + "");
				}
				html.append("<td>");
				html.append("" + UtilStrings.nullToVazio(problema.getDescricao()) + "");
				html.append("</td>");
				html.append("<td>");
				if (problema.getCausaRaiz() == null) {
					html.append("");
				} else {
					html.append("" + problema.getCausaRaiz() + "");
				}
				html.append("</td>");
				html.append("<td>");
				if (problema.getSolucaoContorno() == null) {
					html.append("");
				} else {
					html.append("" + UtilStrings.nullToVazio(problema.getSolucaoContorno()) + "");
				}
				html.append("</td>");
				html.append("</td>");
				html.append("<td>");
				if(problema.getStatus() == null || problema.getStatus() ==""){
					html.append("" + UtilStrings.nullToVazio(problema.getStatus()) + "");
				} else if(problema.getStatus()!= null && problema.getStatus().equalsIgnoreCase("Registrada")){
					html.append("" + UtilI18N.internacionaliza(request, "citcorpore.comum.registrada") + "");
				} else if(problema.getStatus()!= null && problema.getStatus().equalsIgnoreCase("Rejeitada")){
					html.append("" + UtilI18N.internacionaliza(request, "citcorpore.comum.rejeitada") + "");
				} else if(problema.getStatus()!= null && problema.getStatus().equalsIgnoreCase("Cancelada")){
					html.append("" + UtilI18N.internacionaliza(request, "citcorpore.comum.cancelada") + "");
				} else if(problema.getStatus()!= null && problema.getStatus().equalsIgnoreCase("Concluida")){
					html.append("" + UtilI18N.internacionaliza(request, "citcorpore.comum.concluida") + "");
				} else if(problema.getStatus()!= null && problema.getStatus().equalsIgnoreCase("Proposta")){
					html.append("" + UtilI18N.internacionaliza(request, "citcorpore.comum.proposta") + "");
				} else if(problema.getStatus()!= null && problema.getStatus().equalsIgnoreCase("Aprovada")){
					html.append("" + UtilI18N.internacionaliza(request, "citcorpore.comum.aprovada") + "");
				} else if(problema.getStatus()!= null && problema.getStatus().equalsIgnoreCase("Executada")){
					html.append("" + UtilI18N.internacionaliza(request, "citcorpore.comum.executada") + "");
				} else {
					html.append("" + UtilStrings.nullToVazio(problema.getStatus()) + "");
				}
				html.append("</td>");
			}
			html.append("</table>");
			html.append("</div>");
			document.getElementById("dadosProblema").setInnerHTML(html.toString());
			document.executeScript("$('#POPUP_DADOSPROBLEMA').dialog('open')");
		} else {
			document.alert(UtilI18N.internacionaliza(request, "MSG04"));
		}
	}

	public void listarItemConfiguracao(DocumentHTML document, HttpServletRequest request, HttpServletResponse response) throws Exception {

		BaseConhecimentoDTO baseConhecimentoDto = new BaseConhecimentoDTO();

		baseConhecimentoDto = (BaseConhecimentoDTO) document.getBean();

		StringBuffer html = new StringBuffer();

		ItemConfiguracaoDTO itemConfiguracaoDTO = new ItemConfiguracaoDTO();

		ItemConfiguracaoService itemConfiguracaoService = (ItemConfiguracaoService) ServiceLocator.getInstance().getService(ItemConfiguracaoService.class, WebUtil.getUsuarioSistema(request));

		itemConfiguracaoDTO.setIdBaseConhecimento(baseConhecimentoDto.getIdBaseConhecimento());

		Collection<ItemConfiguracaoDTO> listaItemConfiguracaoPorBaseConhecimento = itemConfiguracaoService.listaItemConfiguracaoPorBaseConhecimento(itemConfiguracaoDTO);

		if (listaItemConfiguracaoPorBaseConhecimento != null) {
			html.append("<div>");
			html.append("<table class='table' id='tbRetorno' width='100%' >");
			html.append("<tr>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "citcorpore.comum.identificacao")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "citcorpore.comum.dataCriacao")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "relatorioBaseConhecimento.ordenarPorVersao")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "pesquisaBaseConhecimento.Familia")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "pesquisaBaseConhecimento.dataExpiracao")+"");
			html.append("</th>");
			html.append("</tr>");
			for (ItemConfiguracaoDTO itemConfiguracao : listaItemConfiguracaoPorBaseConhecimento) {
				html.append("<tr>");
				html.append("<td>");
				html.append("" + itemConfiguracao.getIdentificacao() + "");
				html.append("</td>");
				html.append("<td>");
				if (itemConfiguracao.getDataInicio() == null) {
					html.append("");
				} else {
					html.append("" + itemConfiguracao.getDataInicio() + "");
				}
				html.append("</td>");
				html.append("<td>");
				if (itemConfiguracao.getVersao() == null) {
					html.append("");
				} else {
					html.append("" + itemConfiguracao.getVersao() + "");
				}
				html.append("<td>");
				html.append("" + UtilStrings.nullToVazio(itemConfiguracao.getFamilia()) + "");
				html.append("</td>");
				html.append("<td>");
				if (itemConfiguracao.getDataExpiracao() == null) {
					html.append("");
				} else {
					html.append("" + itemConfiguracao.getDataExpiracao() + "");
				}
				html.append("</td>");
			}
			html.append("</table>");
			html.append("</div>");
			document.getElementById("dadosItemConfiguracao").setInnerHTML(html.toString());
			document.executeScript("$('#POPUP_DADOSITEMCONFIGURACAO').dialog('open')");
		} else {
			document.alert(UtilI18N.internacionaliza(request, "MSG04"));
		}
	}

	public void listarMudanca(DocumentHTML document, HttpServletRequest request, HttpServletResponse response) throws Exception {

		BaseConhecimentoDTO baseConhecimentoDto = new BaseConhecimentoDTO();

		baseConhecimentoDto = (BaseConhecimentoDTO) document.getBean();

		StringBuffer html = new StringBuffer();

		RequisicaoMudancaDTO mudancaDTO = new RequisicaoMudancaDTO();

		RequisicaoMudancaService mudancaService = (RequisicaoMudancaService) ServiceLocator.getInstance().getService(RequisicaoMudancaService.class, WebUtil.getUsuarioSistema(request));

		mudancaDTO.setIdBaseConhecimento(baseConhecimentoDto.getIdBaseConhecimento());

		Collection<RequisicaoMudancaDTO> listaMudancaoPorBaseConhecimento = mudancaService.listaMudancaPorBaseConhecimento(mudancaDTO);

		if (listaMudancaoPorBaseConhecimento != null) {
			html.append("<div>");
			html.append("<table class='table' id='tbRetorno' width='100%' >");
			html.append("<tr>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "citcorpore.comum.identificacao")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "problema.proprietario")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "citcorpore.comum.motivo")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "solicitacaoServico.datahoraabertura")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "solicitacaoServico.descricao")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "citcorpore.comum.tipo")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "pesquisaBaseConhecimento.dataHoraConclusao")+"");
			html.append("</th>");
			html.append("<th>");
			html.append(""+UtilI18N.internacionaliza(request, "requisicaMudanca.status")+"");
			html.append("</th>");
			html.append("</tr>");
			for (RequisicaoMudancaDTO mudanca : listaMudancaoPorBaseConhecimento) {
				html.append("<tr>");
				html.append("<td>");
				html.append("" + mudanca.getIdRequisicaoMudanca() + "");
				html.append("</td>");
				html.append("<td>");
				html.append("" + mudanca.getNomeProprietario() + "");
				html.append("</td>");
				html.append("<td>");
				html.append("" + UtilStrings.nullToVazio(mudanca.getMotivo()) + "");
				html.append("</td>");
				html.append("<td>");
				if (mudanca.getDataHoraInicio() == null) {
					html.append("");
				} else {
					html.append("" + UtilDatas.formatTimestamp(mudanca.getDataHoraInicio()) + "");
				}
				html.append("<td>");
				html.append("" + mudanca.getDescricao() + "");
				html.append("</td>");
				html.append("<td>");
				html.append("" + UtilStrings.nullToVazio(mudanca.getTipo()) + "");
				html.append("</td>");
				html.append("<td>");
				if (mudanca.getDataHoraConclusao() == null) {
					html.append("");
				} else {
					html.append("" + UtilDatas.formatTimestamp(mudanca.getDataHoraConclusao()) + "");
				}
				html.append("</td>");
				html.append("<td>");
				if(mudanca.getStatus() == null || mudanca.getStatus() ==""){
					html.append("" + UtilStrings.nullToVazio(mudanca.getStatus()) + "");
				} else if(mudanca.getStatus()!= null && mudanca.getStatus().equalsIgnoreCase("Registrada")){
					html.append("" + UtilI18N.internacionaliza(request, "citcorpore.comum.registrada") + "");
				} else if(mudanca.getStatus()!= null && mudanca.getStatus().equalsIgnoreCase("Rejeitada")){
					html.append("" + UtilI18N.internacionaliza(request, "citcorpore.comum.rejeitada") + "");
				} else if(mudanca.getStatus()!= null && mudanca.getStatus().equalsIgnoreCase("Cancelada")){
					html.append("" + UtilI18N.internacionaliza(request, "citcorpore.comum.cancelada") + "");
				} else if(mudanca.getStatus()!= null && mudanca.getStatus().equalsIgnoreCase("Concluida")){
					html.append("" + UtilI18N.internacionaliza(request, "citcorpore.comum.concluida") + "");
				} else if(mudanca.getStatus()!= null && mudanca.getStatus().equalsIgnoreCase("Proposta")){
					html.append("" + UtilI18N.internacionaliza(request, "citcorpore.comum.proposta") + "");
				} else if(mudanca.getStatus()!= null && mudanca.getStatus().equalsIgnoreCase("Aprovada")){
					html.append("" + UtilI18N.internacionaliza(request, "citcorpore.comum.aprovada") + "");
				} else if(mudanca.getStatus()!= null && mudanca.getStatus().equalsIgnoreCase("Executada")){
					html.append("" + UtilI18N.internacionaliza(request, "citcorpore.comum.executada") + "");
				} else {
					html.append("" + UtilStrings.nullToVazio(mudanca.getStatus()) + "");
				}
				html.append("</td>");
				html.append("</tr>");
			}
			html.append("</table>");
			html.append("</div>");
			document.getElementById("dadosProblema").setInnerHTML(html.toString());
			document.executeScript("$('#POPUP_DADOSPROBLEMA').dialog('open')");
		} else {
			document.alert(UtilI18N.internacionaliza(request, "MSG04"));
		}
	}

	public void listarComentario(DocumentHTML document, HttpServletRequest request, HttpServletResponse response) throws Exception {

		BaseConhecimentoDTO baseConhecimentoDto = new BaseConhecimentoDTO();

		baseConhecimentoDto = (BaseConhecimentoDTO) document.getBean();

		//ComentariosDTO comentariosDTO = new ComentariosDTO();

		//StringBuffer html = new StringBuffer();

		ComentariosService comentariosService = (ComentariosService) ServiceLocator.getInstance().getService(ComentariosService.class, null);

		Collection<ComentariosDTO> comentarios = comentariosService.consultarComentarios(baseConhecimentoDto);

		if (comentarios != null && !comentarios.isEmpty()) {
			document.executeScript("$('#POPUP_DADOSCOMENTARIO').dialog('open')");
			document.executeScript("deleteAllRows()");
			if (comentarios != null && !comentarios.isEmpty()) {
				int count = 0;
				for (ComentariosDTO comentarioBean : comentarios) {
					count++;

					String comentario = (comentarioBean.getComentario() != null ? comentarioBean.getComentario() : "");
					String nome = (comentarioBean.getNome() != null ? comentarioBean.getNome() : "");
					String email = (comentarioBean.getEmail() != null ? comentarioBean.getEmail() : "");
					String dataInicio = (comentarioBean.getDataInicio() != null ? comentarioBean.getDataInicio().toString() : "");
					String nota = (comentarioBean.getNota() != null ? comentarioBean.getNota().toString() : "");

					document.executeScript("restoreRow('" + comentario + "','" + nome + "','" + email + "','" + nota + "')");

				}
				document.executeScript("exibeGrid()");
			} else {
				document.executeScript("ocultaGrid()");
			}
		} else {
			document.alert(UtilI18N.internacionaliza(request, "MSG04"));
		}
	}

	public String calcularMediaBaseConhecimento(BaseConhecimentoDTO baseConhecimento, BaseConhecimentoService baseConhecimentoService) throws Exception {
		Double media = baseConhecimentoService.calcularNota(baseConhecimento.getIdBaseConhecimento());
		if (media != null) {
			return media.toString();
		} else {
			return "-1";
		}
	}

	/**
	 * FireEvent para Exibir hist�rico de Vers�es Anteriores da Base de Conhecimento Selecionada.
	 * 
	 * @param document
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void exibirHistoricoVersoesBaseConhecimento(DocumentHTML document, HttpServletRequest request, HttpServletResponse response) throws Exception {

		BaseConhecimentoDTO baseConhecimentoDto = (BaseConhecimentoDTO) document.getBean();

		BaseConhecimentoService baseConhecimentoService = (BaseConhecimentoService) ServiceLocator.getInstance().getService(BaseConhecimentoService.class, null);
		if (baseConhecimentoDto.getIdBaseConhecimento() != null) {

			baseConhecimentoDto = (BaseConhecimentoDTO) baseConhecimentoService.restore(baseConhecimentoDto);
		}

		document.executeScript("deleteAllRowsHistoricoVersoes();");

		Collection<BaseConhecimentoDTO> listVersoesBaseConhecimento = baseConhecimentoService.obterHistoricoDeVersoes(baseConhecimentoDto);

		if (listVersoesBaseConhecimento != null && !listVersoesBaseConhecimento.isEmpty()) {

			for (BaseConhecimentoDTO baseConhecimentoVersao : listVersoesBaseConhecimento) {

				document.executeScript("addLinhaTabelaHistorioVersoes(" + baseConhecimentoVersao.getIdBaseConhecimento() + ",'" + baseConhecimentoVersao.getTitulo() + "'," + true + ","
						+ baseConhecimentoVersao.getVersao() + ");");

			}
			document.executeScript("$('#POPUP_HISTORICOVERSAO').dialog('open');");
		} else {
			document.alert(UtilI18N.internacionaliza(request, "MSG04"));
		}

	}

	/**
	 * FireEvent para Exibir hist�rico de Altera��es da Base de Conhecimento Selecionada.
	 * 
	 * @param document
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void exibirHistoricoAlteracaoBaseConhecimento(DocumentHTML document, HttpServletRequest request, HttpServletResponse response) throws Exception {

		BaseConhecimentoDTO baseConhecimentoDto = (BaseConhecimentoDTO) document.getBean();

		BaseConhecimentoService baseConhecimentoService = (BaseConhecimentoService) ServiceLocator.getInstance().getService(BaseConhecimentoService.class, null);
		HistoricoBaseConhecimentoService historicoBaseConhecimentoService = (HistoricoBaseConhecimentoService) ServiceLocator.getInstance().getService(HistoricoBaseConhecimentoService.class, null);
		PastaService pastaService = (PastaService) ServiceLocator.getInstance().getService(PastaService.class, null);
		UsuarioService usuarioService = (UsuarioService) ServiceLocator.getInstance().getService(UsuarioService.class, null);

		HistoricoBaseConhecimentoDTO historicoBaseConhecimentoDto = new HistoricoBaseConhecimentoDTO();

		PastaDTO pastaDto = new PastaDTO();

		UsuarioDTO usuarioDto = new UsuarioDTO();

		String dataHoraAlteracao = "";

		if (baseConhecimentoDto.getIdBaseConhecimento() != null) {

			baseConhecimentoDto = (BaseConhecimentoDTO) baseConhecimentoService.restore(baseConhecimentoDto);
		}

		if (baseConhecimentoDto.getIdHistoricoBaseConhecimento() != null) {
			historicoBaseConhecimentoDto.setIdHistoricoBaseConhecimento(baseConhecimentoDto.getIdHistoricoBaseConhecimento());
		}
		document.executeScript("deleteAllRowsHistoricoAlteracao();");

		Collection<HistoricoBaseConhecimentoDTO> listHistoricoAlteracaoBaseConhecimento = historicoBaseConhecimentoService.obterHistoricoDeAlteracao(historicoBaseConhecimentoDto);

		if (listHistoricoAlteracaoBaseConhecimento != null && !listHistoricoAlteracaoBaseConhecimento.isEmpty()) {

			for (HistoricoBaseConhecimentoDTO historicoBaseConhecimento : listHistoricoAlteracaoBaseConhecimento) {

				if (historicoBaseConhecimento.getIdPasta() != null) {
					pastaDto.setId(historicoBaseConhecimento.getIdPasta());
					pastaDto = (PastaDTO) pastaService.restore(pastaDto);
				}
				if (historicoBaseConhecimento.getIdUsuarioAlteracao() != null) {
					usuarioDto.setIdUsuario(historicoBaseConhecimento.getIdUsuarioAlteracao());
					usuarioDto = (UsuarioDTO) usuarioService.restore(usuarioDto);
				}

				if (historicoBaseConhecimento.getStatus().equalsIgnoreCase("S")) {
					historicoBaseConhecimento.setStatus(UtilI18N.internacionaliza(request, "pesquisaBaseConhecimento.publicado"));
				} else {
					historicoBaseConhecimento.setStatus(UtilI18N.internacionaliza(request, "pesquisaBaseConhecimento.naoPublicado"));
				}
				if (historicoBaseConhecimento.getDataHoraAlteracao() != null) {

					dataHoraAlteracao = UtilDatas.formatTimestamp(historicoBaseConhecimento.getDataHoraAlteracao());
				}

				document.executeScript("addLinhaTabelaHistorioAlteracao('" + historicoBaseConhecimento.getTitulo() + "','" + pastaDto.getNome() + "','" + historicoBaseConhecimento.getVersao() + "','"
						+ historicoBaseConhecimento.getOrigem() + "','" + usuarioDto.getNomeUsuario() + "','" + dataHoraAlteracao + "','" + historicoBaseConhecimento.getStatus() + "');");

			}

			document.executeScript("$('#POPUP_ALTERACAO').dialog('open');");
		} else {
			document.alert(UtilI18N.internacionaliza(request, "MSG04"));
		}

	}
	
	public void atualizaGridProblema(DocumentHTML document, HttpServletRequest request, HttpServletResponse response) throws Exception {
		BaseConhecimentoDTO baseConhecimentoDto = new BaseConhecimentoDTO();
		baseConhecimentoDto = (BaseConhecimentoDTO) document.getBean();
		
		ProblemaService problemaService = (ProblemaService) ServiceLocator.getInstance().getService(ProblemaService.class, null);
		ProblemaDTO problemaDTO = new ProblemaDTO();

		problemaDTO.setIdProblema(baseConhecimentoDto.getIdProblema());
		
		problemaDTO = (ProblemaDTO) problemaService.restore(problemaDTO);
		if (problemaDTO == null) {
			return;
		}
		HTMLTable tblProblema = document.getTableById("tblProblema");

		if (problemaDTO.getSequenciaProblema() == null) {
			tblProblema.addRow(problemaDTO, new String[] { "", "", "numberAndTitulo", "status" }, new String[] { "idProblema" }, UtilI18N.internacionaliza(request, "baseConhecimento.problemaExiste"), new String[] { "exibeIconesProblema" }, "buscaProblema", null);
		} else {
			tblProblema.updateRow(problemaDTO, new String[] { "", "", "numberAndTitulo", "status" }, new String[] { "idProblema" }, UtilI18N.internacionaliza(request, "baseConhecimento.problemaExiste"), new String[] { "exibeIconesProblema"}, "buscaProblema",
					null, problemaDTO.getSequenciaProblema());
		}
		document.executeScript("HTMLUtils.applyStyleClassInAllCells('tblProblema', 'tblProblema');");
		document.executeScript("fecharProblemaAtualizaGrid();");

		baseConhecimentoDto = null;
	}
	
//	public void gravarProblemasConhecimento(BaseConhecimentoDTO baseConhecimento) throws Exception {
//
//		if (baseConhecimento.getIdBaseConhecimento() != null) {
//			ConhecimentoProblemaDao conhecimentoProblemaDAO = new ConhecimentoProblemaDao();
//
//			conhecimentoProblemaDAO.deleteByIdBaseConhecimento(baseConhecimento.getIdBaseConhecimento());
//
//			try {
//				if (baseConhecimento.getColItensProblema() != null) {
//
//					for (Iterator it = baseConhecimento.getColItensProblema().iterator(); it.hasNext();) {
//
//						ProblemaDTO problemaDTO = (ProblemaDTO) it.next();
//
//						ConhecimentoProblemaDTO conhecimentoProblemaDTO = new ConhecimentoProblemaDTO();
//
//						conhecimentoProblemaDTO.setIdProblema(problemaDTO.getIdProblema());
//						conhecimentoProblemaDTO.setIdBaseConhecimento(baseConhecimento.getIdBaseConhecimento());
//
//						conhecimentoProblemaDAO.create(conhecimentoProblemaDTO);
//					}
//				}
//				
//			} catch (Exception e) {
//				System.err.println(e);
//			}
//		}
//	}

}
