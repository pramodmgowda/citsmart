package br.com.centralit.citcorpore.integracao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.centralit.citcorpore.bean.JustificativaRequisicaoMudancaDTO;
import br.com.citframework.dto.IDto;
import br.com.citframework.integracao.CrudDaoDefaultImpl;
import br.com.citframework.integracao.Field;
import br.com.citframework.util.Constantes;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class JustificativaRequisicaoMudancaDao extends CrudDaoDefaultImpl {

	public JustificativaRequisicaoMudancaDao() {
		super(Constantes.getValue("DATABASE_ALIAS"), null);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Collection find(IDto obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection getFields() {
		Collection listFields = new ArrayList();
		listFields.add(new Field("idjustificativamudanca", "idJustificativaMudanca", true, true, false, false));
		listFields.add(new Field("descricaojustificativa", "descricaoJustificativa", false, false, false, false));
		listFields.add(new Field("suspensao", "suspensao", false, false, false, false));
		listFields.add(new Field("situacao", "situacao", false, false, false, false));
		listFields.add(new Field("aprovacao", "aprovacao", false, false, false, false));
		listFields.add(new Field("deleted", "deleted", false, false, false, false));
		return listFields;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "justificativamudanca";
	}

	@Override
	public Collection list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class getBean() {
		// TODO Auto-generated method stub
		return JustificativaRequisicaoMudancaDTO.class;
	}

	public Collection<JustificativaRequisicaoMudancaDTO> listAtivasParaSuspensao() throws Exception {
		
		List  listRetorno = new ArrayList();
		List  parametros = new ArrayList();
		
		
		StringBuffer sql =  new StringBuffer();
		sql.append("select idjustificativamudanca, descricaojustificativa from "+getTableName()+" where suspensao = ? and situacao = ? and (deleted is null or deleted <> ? ) ");
		parametros.add("S");
		parametros.add("A");
		parametros.add("Y");
		
		
		List lista = this.execSQL(sql.toString(), parametros.toArray());
		listRetorno.add("idJustificativaMudanca");
		listRetorno.add("descricaoJustificativa");
		
		if(lista !=null && !lista.isEmpty()){
			return this.engine.listConvertion(getBean(), lista, listRetorno);
		}
		
		return null;
	}

}
