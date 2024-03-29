/**
 * CentralIT - CITSmart
 */
package br.com.centralit.citcorpore.negocio;

import java.util.Collection;

import br.com.centralit.citcorpore.bean.CategoriaPostDTO;
import br.com.citframework.excecao.PersistenceException;
import br.com.citframework.excecao.ServiceException;
import br.com.citframework.service.CrudServiceEjb2;

@SuppressWarnings("rawtypes")
public interface CategoriaPostService extends CrudServiceEjb2 {

	/**
	 * Retorna Lista de Categorias Ativas.
	 * 
	 * @throws Exception
	 * @return <code>Collection</code>
	 */
	public Collection listCategoriasAtivas() throws Exception;

	/**
	 * Verifica se Categoria Servi�o possui subcategoria ou servi�o associado.
	 * 
	 * @param categoriaServico
	 * @return - <b>True:</b> Possui. - <b>False: </b>N�o possui.
	 * @throws PersistenceException
	 * @throws ServiceException
	 */
	public boolean verificarSeCategoriaPossuiServicoOuSubCategoria(CategoriaPostDTO categoriaPostDTO) throws PersistenceException, br.com.citframework.excecao.ServiceException;

	/**
	 * Verifica se categoria informada j� existe.
	 * 
	 * @param categoriaServicoDTO
	 * @throws PersistenceException
	 * @throws ServiceException
	 * @return true - existe; false - n�o existe;
	 */
	public boolean verificarSeCategoriaExiste(CategoriaPostDTO categoriaPostDTO) throws PersistenceException, ServiceException;


}
