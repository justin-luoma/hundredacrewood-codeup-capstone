package support.onehundredacrewood.app.dao.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserWithRoles extends User implements UserDetails {
    private boolean admin;
    private boolean disabled;

    public UserWithRoles(User user) {
        super(user);  // Call the copy constructor defined in User
        this.admin = user.isAdmin();
        this.disabled = user.isDisabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roles = "USER";
        if (admin) {
            roles = "ADMIN,USER";
        }
        return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
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
        return !this.disabled;
    }
}
