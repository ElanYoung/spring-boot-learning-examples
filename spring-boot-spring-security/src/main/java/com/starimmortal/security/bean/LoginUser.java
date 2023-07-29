package com.starimmortal.security.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.starimmortal.security.enumeration.UserStatusEnum;
import com.starimmortal.security.pojo.MenuDO;
import com.starimmortal.security.pojo.UserDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 登录用户
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class LoginUser extends UserDO implements UserDetails {

	/**
	 * 菜单集合
	 */
	private List<MenuDO> menuList;

	/**
	 * 权限集合
	 */
	@JsonIgnore
	private List<SimpleGrantedAuthority> authorities;

	public LoginUser(UserDO user, List<MenuDO> menuList) {
		BeanUtils.copyProperties(user, this);
		this.menuList = menuList;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (Objects.nonNull(authorities)) {
			return authorities;
		}
		return menuList.stream()
			.filter(menu -> StringUtils.hasText(menu.getPermission()))
			.map(menu -> new SimpleGrantedAuthority(menu.getPermission()))
			.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return super.getPassword();
	}

	@Override
	public String getUsername() {
		return super.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return super.getStatus().equals(UserStatusEnum.ENABLE.getValue());
	}

}
