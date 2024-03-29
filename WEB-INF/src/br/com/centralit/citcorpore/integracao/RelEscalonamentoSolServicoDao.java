package br.com.centralit.citcorpore.integracao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.centralit.citajax.util.Constantes;
import br.com.centralit.citcorpore.bean.RelEscalonamentoSolServicoDto;
import br.com.citframework.dto.IDto;
import br.com.citframework.integracao.CrudDaoDefaultImpl;
import br.com.citframework.integracao.Field;

public class RelEscalonamentoSolServicoDao extends CrudDaoDefaultImpl {

	public RelEscalonamentoSolServicoDao() {
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
	public Collection<Field> getFields() {
		Collection<Field> listFields = new ArrayList<Field>();
		listFields.add(new Field("idSolicitacaoServico", "idSolicitacaoServico", false, false, false, false));
		listFields.add(new Field("idEscalonamento", "idEscalonamento", false, false, false, false));
		return listFields;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "relEscalonamentoSolServico";
	}

	@Override
	public Collection list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class getBean() {
		// TODO Auto-generated method stub
		return RelEscalonamentoSolServicoDto.class;
	}
	
	/**
	 * Verifica se existe relacionamento. Se existir retorna 'true', senao retorna 'false';
	 * 
	 * @param 
	 * @return exists
	 * @throws Exception
	 */
	public boolean temRelacionamentoSolicitacaoEscalonamento(Integer idSolicitacaoServico, Integer idEscalonamento) throws Exception {
		boolean exists;
		List<Integer> parametro = new ArrayList<Integer>();
		List list = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from " + getTableName() + " where idSolicitacaoServico = ? and idEscalonamento = ?  ");
		parametro.add(idSolicitacaoServico);
		parametro.add(idEscalonamento);
		list = this.execSQL(sql.toString(), parametro.toArray());
		if (list != null && !list.isEmpty()) {
			exists = true;
		} else {
			exists = false;
		}
		return exists;
	}

}
