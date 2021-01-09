package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;

	protected MenuGroup() {
	}

	private MenuGroup(String name) {
		this.name = name;
	}

	public static MenuGroup create(String name) {
		return new MenuGroup(name);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}