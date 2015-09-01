package br.com.extratosfacil.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.extratosfacil.beans.BeanPedagio;
import br.com.extratosfacil.entities.pedagio.Concessionaria;

@FacesConverter("concessionariaConverter")
public class ConcessionariaConverter implements Converter {

	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		if (value != null && !value.isEmpty()) {
			BeanPedagio bean = (BeanPedagio) fc.getExternalContext()
					.getApplicationMap().get("beanPedagio");
			if (bean != null) {
				return bean.getConcessionarias().get(
						Integer.parseInt(value) - 1);
			}

			return (Concessionaria) uic.getAttributes().get(value);
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext facesContext,
			UIComponent uiComponent, Object value) {
		if (value instanceof Concessionaria) {
			Concessionaria entity = (Concessionaria) value;
			if (entity != null && entity instanceof Concessionaria
					&& entity.getId() != null) {
				uiComponent.getAttributes().put(entity.getId().toString(),
						entity);
				return entity.getId().toString();
			}
		}
		return "";
	}
}
