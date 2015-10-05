package br.com.extratosfacil.converter;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.extratosfacil.entities.location.Estado;

@FacesConverter(value = "estadoConverter")
@ManagedBean(eager = true)
@ApplicationScoped
public class EstadoConverter implements Converter {

	public static Estado estado = null;

	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		if (value != null && !value.isEmpty()) {
			estado = (Estado) uic.getAttributes().get(value);
			return estado;
		}
		return estado;
	}

	@Override
	public String getAsString(FacesContext facesContext,
			UIComponent uiComponent, Object value) {
		if (value instanceof Estado) {
			Estado entity = (Estado) value;
			if (entity != null && entity instanceof Estado
					&& entity.getId() != null) {
				uiComponent.getAttributes().put(entity.getId().toString(),
						entity);
				return entity.getId().toString();
			}
		}
		return "";
	}
}
