package br.com.extratosfacil.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.extratosfacil.entities.location.Cidade;

@FacesConverter(value = "cidadeConverter")
public class CidadeConverter implements Converter {

	public static Cidade cidade = null;

	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		if (value != null && !value.isEmpty()) {
			cidade = (Cidade) uic.getAttributes().get(value);
			return cidade;
		}
		return cidade;
	}

	@Override
	public String getAsString(FacesContext facesContext,
			UIComponent uiComponent, Object value) {
		if (value instanceof Cidade) {
			Cidade entity = (Cidade) value;
			if (entity != null && entity instanceof Cidade
					&& entity.getId() != null) {
				uiComponent.getAttributes().put(entity.getId().toString(),
						entity);
				return entity.getId().toString();
			}
		}
		return "";
	}
}
