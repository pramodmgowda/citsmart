<%@page import="br.com.centralit.citcorpore.util.CitCorporeConstantes"%>
<%@page import="br.com.centralit.citcorpore.util.ParametroUtil"%>
<%
	String PAGINA_SOLICITACAO_SERVICO = ParametroUtil.getValorParametroCitSmartHashMap(br.com.centralit.citcorpore.util.Enumerados.ParametroSistema.PAGE_CADADTRO_SOLICITACAOSERVICO, "");
%>	
<head>

<style type="text/css">
.btn{font-weight:600;}
.btn-block{width:100%;}
.btn + .btn:not(.btn-block){margin:0 0 0 5px;}
.btn-group .btn + .btn:not(.btn-block){margin:0;}
.btn-default{text-shadow:0 1px 0 rgba(255, 255, 255, 0.75);color:rgba(0, 0, 0, 0.6);text-shadow:0 1px 0 #fff;font-weight:600;background-color:#efefef;background-image:-moz-linear-gradient(top, #f4f4f4, #e7e7e7);background-image:-webkit-gradient(linear, 0 0, 0 100%, from(#f4f4f4), to(#e7e7e7));background-image:-webkit-linear-gradient(top, #f4f4f4, #e7e7e7);background-image:-o-linear-gradient(top, #f4f4f4, #e7e7e7);background-image:linear-gradient(to bottom, #f4f4f4, #e7e7e7);background-repeat:repeat-x;filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#fff4f4f4', endColorstr='#ffe7e7e7', GradientType=0);border:1px solid #e7e7e7;box-shadow:0 1px 0 0 #ffffff inset, 0 0 0 1px #cecece;-moz-box-shadow:0 1px 0 0 #ffffff inset, 0 0 0 1px #cecece;-webkit-box-shadow:0 1px 0 0 #ffffff inset, 0 0 0 1px #cecece;}
.btn-group.open .btn-default.dropdown-toggle,
.btn-default.disabled,
.btn-default[disabled],
.btn-default:hover,
.btn-default:focus{background:#e8e8e8;}
.btn-default:active,
.btn-default.active{background:#e8e8e8;color:#fff;text-shadow:none;}
.btn-default .caret{border-top-color:rgba(0, 0, 0, 0.5);}
.btn-warning{background-color:#b6895e;background-image:-moz-linear-gradient(top, #be946b, #ab7a4b);background-image:-webkit-gradient(linear, 0 0, 0 100%, from(#be946b), to(#ab7a4b));background-image:-webkit-linear-gradient(top, #be946b, #ab7a4b);background-image:-o-linear-gradient(top, #be946b, #ab7a4b);background-image:linear-gradient(to bottom, #be946b, #ab7a4b);background-repeat:repeat-x;filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffbe946b', endColorstr='#ffab7a4b', GradientType=0);box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #88613b;-moz-box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #88613b;-webkit-box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #88613b;border:1px solid #ab7a4b;color:#ffffff;text-shadow:0 1px 0 rgba(0, 0, 0, 0.5);}
.btn-warning.glyphicons i:before{color:rgba(255, 255, 255, 0.5);text-shadow:none;}
.btn-group.open .btn-warning.dropdown-toggle,
.btn-warning.disabled,
.btn-warning[disabled],
.btn-warning:hover,
.btn-warning:focus{background:#ab7a4b;color:#fff;}
.btn-warning:active,
.btn-warning.active{background:#ab7a4b;color:#fff;}
.btn-warning .caret{border-top-color:rgba(255, 255, 255, 0.7);border-bottom-color:rgba(255, 255, 255, 0.7);}
.btn-info{background:#93B9D8;border:1px solid #93B9D8;color:#ffffff;text-shadow:0 1px 0 rgba(0, 0, 0, 0.5);}
.btn-info.glyphicons i:before{color:rgba(255, 255, 255, 0.5);text-shadow:none;}
.btn-group.open .btn-info.dropdown-toggle,
.btn-info.disabled,
.btn-info[disabled],
.btn-info:hover,
.btn-info:focus{background:#93B9D8;color:#fff;}
.btn-info:active,
.btn-info.active{background:#93B9D8;color:#fff;}
.btn-info .caret{border-top-color:rgba(255, 255, 255, 0.7);border-bottom-color:rgba(255, 255, 255, 0.7);}
.btn-danger{background-color:#ca4741;background-image:-moz-linear-gradient(top, #d3534c, #bd362f);background-image:-webkit-gradient(linear, 0 0, 0 100%, from(#d3534c), to(#bd362f));background-image:-webkit-linear-gradient(top, #d3534c, #bd362f);background-image:-o-linear-gradient(top, #d3534c, #bd362f);background-image:linear-gradient(to bottom, #d3534c, #bd362f);background-repeat:repeat-x;filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffd3534c', endColorstr='#ffbd362f', GradientType=0);border:1px solid #bd362f;box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #942a25;-moz-box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #942a25;-webkit-box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #942a25;color:#ffffff;text-shadow:0 1px 0 rgba(0, 0, 0, 0.5);}
.btn-danger.glyphicons i:before{color:rgba(255, 255, 255, 0.5);text-shadow:none;}
.btn-group.open .btn-danger.dropdown-toggle,
.btn-danger.disabled,
.btn-danger[disabled],
.btn-danger:hover,
.btn-danger:focus{background:#bd362f;color:#fff;}
.btn-danger:active,
.btn-danger.active{background:#bd362f;color:#fff;}
.btn-danger .caret{border-top-color:rgba(255, 255, 255, 0.7);border-bottom-color:rgba(255, 255, 255, 0.7);}
.btn-primary{background-color:#9dce6e;background-image:-moz-linear-gradient(top, #a8d37d, #8ec657);background-image:-webkit-gradient(linear, 0 0, 0 100%, from(#a8d37d), to(#8ec657));background-image:-webkit-linear-gradient(top, #a8d37d, #8ec657);background-image:-o-linear-gradient(top, #a8d37d, #8ec657);background-image:linear-gradient(to bottom, #a8d37d, #8ec657);background-repeat:repeat-x;filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffa8d37d', endColorstr='#ff8ec657', GradientType=0);border:1px solid #8ec657;box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #74af3b;-moz-box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #74af3b;-webkit-box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #74af3b;color:#ffffff;text-shadow:0 1px 0 rgba(0, 0, 0, 0.5);}
.btn-primary.glyphicons i:before{color:rgba(255, 255, 255, 0.4);text-shadow:none;}
.btn-group.open .btn-primary.dropdown-toggle,
.btn-primary.disabled,
.btn-primary[disabled],
.btn-primary:hover,
.btn-primary:focus{background:#8ec657;color:#fff;}
.btn-primary:active,
.btn-primary.active{background:#8ec657;color:#fff;}
.btn-primary .caret{border-top-color:#ffffff;border-bottom-color:#ffffff;}
.btn-inverse{background-color:#53575d;background-image:-moz-linear-gradient(top, #5d6168, #45484d);background-image:-webkit-gradient(linear, 0 0, 0 100%, from(#5d6168), to(#45484d));background-image:-webkit-linear-gradient(top, #5d6168, #45484d);background-image:-o-linear-gradient(top, #5d6168, #45484d);background-image:linear-gradient(to bottom, #5d6168, #45484d);background-repeat:repeat-x;filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff5d6168', endColorstr='#ff45484d', GradientType=0);border:1px solid #45484d;box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #2d2f32;-moz-box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #2d2f32;-webkit-box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #2d2f32;color:#ffffff;text-shadow:0 1px 0 rgba(0, 0, 0, 0.5);}
.btn-inverse.glyphicons i:before{color:#919293;text-shadow:none;}
.btn-group.open .btn-inverse.dropdown-toggle,
.btn-inverse.disabled,
.btn-inverse[disabled],
.btn-inverse:hover,
.btn-inverse:focus{background:#45484d;color:#fff;}
.btn-inverse:active,
.btn-inverse.active{background:#45484d;color:#fff;}
.btn-inverse .caret{border-top-color:#fff;border-bottom-color:#fff;}
.btn-success{background-color:#6fa45f;background-image:-moz-linear-gradient(top, #79ae69, #609450);background-image:-webkit-gradient(linear, 0 0, 0 100%, from(#79ae69), to(#609450));background-image:-webkit-linear-gradient(top, #79ae69, #609450);background-image:-o-linear-gradient(top, #79ae69, #609450);background-image:linear-gradient(to bottom, #79ae69, #609450);background-repeat:repeat-x;filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff79ae69', endColorstr='#ff609450', GradientType=0);border:1px solid #609450;box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #4b733e;-moz-box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #4b733e;-webkit-box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #4b733e;color:#ffffff;text-shadow:0 1px 0 rgba(0, 0, 0, 0.5);}
.btn-success.glyphicons i:before{color:#a2bc9b;text-shadow:none;}
.btn-group.open .btn-success.dropdown-toggle,
.btn-success.disabled,
.btn-success[disabled],
.btn-success:hover,
.btn-success:focus{background:#609450;color:#fff;}
.btn-success:active,
.btn-success.active{background:#609450;color:#fff;}
.btn-success .caret{border-top-color:#fff;border-bottom-color:#fff;}
.btn-group .btn-primary:not(.dropdown-toggle){margin-right:2px;}
.btn-group-vertical.block{display:block;}
.btn-group.btn-block{padding:0;position:relative;}
.btn-group.btn-block .leadcontainer{left:0;position:absolute;right:29px;}
.btn-group.btn-block .leadcontainer > .btn:first-child{-webkit-border-radius:0 0 0 0;-moz-border-radius:0 0 0 0;border-radius:0 0 0 0;margin-left:0;-webkit-border-bottom-left-radius:4px;border-bottom-left-radius:4px;-webkit-border-top-left-radius:4px;border-top-left-radius:4px;-moz-border-radius-bottomleft:4px;-moz-border-radius-topleft:4px;text-align:left;}
.btn-group.btn-block .dropdown-lead{box-sizing:border-box;width:100%;}
.btn-group.btn-block .dropdown-toggle{width:30px;float:right;box-sizing:border-box;text-align:center;padding-left:0;padding-right:0;}
.btn-icon-stacked{text-align:left;padding:5px 5px 5px 45px;}
.btn-icon-stacked i{display:block;width:24px;height:24px;position:absolute;left:10px;top:10px;}
.btn-icon-stacked i:before{left:auto;top:auto;position:relative;text-align:center;}
.btn-icon-stacked span{display:block;font-size:13px;line-height:normal;}
.btn-facebook{color:#fff;text-shadow:0 1px 0 rgba(0, 0, 0, 0.1);background-color:#728dc0;background-image:-moz-linear-gradient(top, #8098c6, #5d7cb6);background-image:-webkit-gradient(linear, 0 0, 0 100%, from(#8098c6), to(#5d7cb6));background-image:-webkit-linear-gradient(top, #8098c6, #5d7cb6);background-image:-o-linear-gradient(top, #8098c6, #5d7cb6);background-image:linear-gradient(to bottom, #8098c6, #5d7cb6);background-repeat:repeat-x;filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff8098c6', endColorstr='#ff5d7cb6', GradientType=0);border:1px solid #5d7cb6;box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #46639a;-moz-box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #46639a;-webkit-box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #46639a;}
.btn-facebook i:before{color:#fff;}
.btn-facebook:hover{background:#8098c6;color:#fff;}
.btn-google{color:#fff;text-shadow:0 1px 0 rgba(0, 0, 0, 0.1);background-color:#b24343;background-image:-moz-linear-gradient(top, #bf4a4a, #9e3838);background-image:-webkit-gradient(linear, 0 0, 0 100%, from(#bf4a4a), to(#9e3838));background-image:-webkit-linear-gradient(top, #bf4a4a, #9e3838);background-image:-o-linear-gradient(top, #bf4a4a, #9e3838);background-image:linear-gradient(to bottom, #bf4a4a, #9e3838);background-repeat:repeat-x;filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffbf4a4a', endColorstr='#ff9e3838', GradientType=0);border:1px solid #9e3838;box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #782b2b;-moz-box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #782b2b;-webkit-box-shadow:0 1px 0 0 rgba(255, 255, 255, 0.3) inset, 0 0 0 1px #782b2b;}
.btn-google i:before{color:#fff;}
.btn-google:hover{background:#bf4a4a;color:#fff;}
.btn-toggle-code{position:absolute;bottom:0;right:0;z-index:1000;-webkit-border-radius:0 0 0 0;-moz-border-radius:0 0 0 0;border-radius:0 0 0 0;}
.btn-toggle-code.outside{bottom:-24px;}

/* N�o retirar porque em algumas telas fica faltando esta configura��o */
 #modal_Telefonia{
	
	width: 700px!important;
	/*  left: 70%!important;  */
	z-index: 10001!important;
	top: 20%!important;
}

#modal_Telefonia .modal-body{
	max-height: 300px;
	overflow: auto!important;
}
 #modal_ramalTelefone{
	
	width: 700px!important;
	/*  left: 70%!important;  */
	z-index: 10001!important;
	top: 20%!important;
}

#modal_ramalTelefone .modal-body{
	max-height: 300px;
	overflow: auto!important;
}

#modal_INCIDENTE{

	 width:80%;
	 margin-left:-40%;
	 margin-top:1%!important;
	 top:1%!important;
}

#modal_INCIDENTE .modal-body{
	max-height: 720px;
	overflow: auto!important;
}
 
 
/* .modal-body{max-height:720px!important;overflow:auto!important;}
.modal{width:80%;margin-left:-40%;margin-top:1%!important;top:1%!important;}
@media (max-width:768px){.modal{width:90%;margin-left:2%;}
} */

</style>
</head>
<script>
		function exibirNotificacaoAsterisk(lista){
			document.formCtrlAsterisk.listaChamadas.value = lista;
			document.formCtrlAsterisk.fireEvent("abrePopUpAsterisk");
		}
		
        function ativaClienteAsterisk() {
        	<%
    		String asteriskAtivo = ParametroUtil.getValorParametroCitSmartHashMap(ParametroSistema.SERVASTERISKATIVAR, "N");
    		if (asteriskAtivo.equals("S")) {
    		%>
        		dwr.engine.setActiveReverseAjax(true);
        		dwr.engine.setErrorHandler(errorHandler);
        	<%
    		}
        	%>
        }
        
        $(function() {
        	ativaClienteAsterisk();
         });
        
        function errorHandler(message, ex) {
   			dwr.util.setValue("error", "Cannot connect to server. Initializing retry logic.", {escapeHtml:false});
   		}
		
		function abreRamalTelefone(){
			document.formCtrlAsterisk.fireEvent("abreRamalTelefone");
			$('#modal_ramalTelefone').on('shown.bs.modal', function(){
				$("#ramalTelefone").focus();					
			});
		}
		
		gravarRamalTelefone = function(){
			document.formCtrlAsterisk.fireEvent("gravarRamalTelefone");
		}
		
		function abrirSolicitacao(){
			var idEmpregado = $('#idEmpregado').val();
			var id =  parseInt(idEmpregado);
			/**
			* Motivo: Adicionando a url o parametro 'modalAsterisk=true' para possivel callback
			* Autor: flavio.santana
			* Data/Hora: 11/12/2013 10:00
			**/
			$('#conteudoframeSolicitacaoServico').html("<iframe height='700' id='frameSolicitacaoServico' class='iframeSemBorda' width='100%' src ='<%=CitCorporeConstantes.CAMINHO_SERVIDOR%><%=request.getContextPath()%>/pages/solicitacaoServicoMultiContratos/solicitacaoServicoMultiContratos.load?iframe=true&modalAsterisk=true&idEmpregado="+ id + "'></iframe>");
			$("#modal_INCIDENTE").modal('show');
		}
		
		//N�o usado
		function fecharSolicitacao() {
			$("#modal_INCIDENTE").modal('hide');
			$("#modal_Telefonia").modal('hide');
		}
		/**
		* Motivo: Depois de adicionado a url o parametro 'modalAsterisk=true' o callback da solicita��o chama a fun��o abaixo
		* Autor: flavio.santana
		* Data/Hora: 11/12/2013 10:00
		**/
		function fecharModalSolicitacaoAsterisk() {
			 /**
			* Motivo: Fecha o modal telefonia em qualquer das circunst�ncias
			* Autor: flavio.santana
			* Data/Hora: 11/12/2013 10:00
			**/
			$('#modal_INCIDENTE').on('hide', function(){
				$("#modal_Telefonia").modal('hide');
			});
		}
		
		// function pesquisarItensFiltro() {
		//	 fecharModalNovaSolicitacao();
		// }
		
		$(document).ready(function () {
			   $('input').keypress(function (e) {
			        var code = null;
			        code = (e.keyCode ? e.keyCode : e.which);                
			        return (code == 13) ? false : true;
			   });
		});
		
</script>
			<div class="modal hide fade in" id="modal_ramalTelefone" aria-hidden="false" data-width='500'>
				<!-- Modal heading -->
				<div class="modal-header">
				 <button type="button" class="close" data-dismiss="modal" aria-hidden="true">�</button> 
					<h3><i18n:message key="citcorpore.comum.ramal"/></h3>
				</div>
				<!-- // Modal heading END -->
				<!-- Modal body -->
				<div class="modal-body">
					<div class='slimScrollDiv'>
						<div class='slim-scroll' id='contentFrameOrigem'>
								<div id="conteudoframeRamalTelefone">
									<form id="formCtrlAsterisk" name="formCtrlAsterisk" action='<%=CitCorporeConstantes.CAMINHO_SERVIDOR%><%=request.getContextPath()%>/pages/ctrlAsterisk/ctrlAsterisk'>
										<input type='hidden' id='listaChamadas' name='listaChamadas'/>
										<input autofocus="autofocus" type='text' name="ramalTelefone" id="ramalTelefone" maxlength="20" size="20" class="Description[citcorpore.comum.ramal.descricao]"/>
									</form>
								</div>
						</div>
					</div>
				</div>
				<!-- // Modal body END -->
				<!-- Modal footer -->
				<div class="modal-footer">
					<div style="margin: 0;" class="form-actions">
						<a href="#" class="btn " onclick="gravarRamalTelefone();" data-dismiss="modal"><i18n:message key="citcorpore.comum.gravar" /></a> 
					</div>
					<!-- // Modal footer END -->
				</div>
			</div>
			
			<div class="modal hide fade in" id="modal_Telefonia" aria-hidden="false" data-width='500'>
				<!-- Modal heading -->
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">�</button>
					<h3><i18n:message key="citcorpore.comum.telefone.vcRecebendoLigacao" /></h3>
				</div>
				<!-- // Modal heading END -->
				<!-- Modal body -->
				<div class="modal-body">
					<div class='slimScrollDiv'>
						<div class='slim-scroll' id='contentFrameOrigem'>
							<div id="conteudoframeTelefonia">
							</div>
						</div>
					</div>
				</div>
				<!-- // Modal body END -->
				<!-- Modal footer -->
				<div class="modal-footer">
					<div style="margin: 0;" class="form-actions">
					</div>
					<!-- // Modal footer END -->
				</div>
			</div>
			
			<div class="modal hide fade in" id="modal_INCIDENTE" tabindex="-1" data-backdrop="static" data-keyboard="false">
					<!-- Modal heading -->
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">�</button>
						<h3><i18n:message key='solicitacaoServico.solicitacao' /></h3>
					</div>
					<!-- // Modal heading END -->
					<!-- Modal body -->
					<div class="modal-body">
						<div id="conteudoframeSolicitacaoServico">
							
						</div>
					</div>
				</div>
