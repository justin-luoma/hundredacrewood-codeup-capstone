package support.onehundredacrewood.app.security;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Argon2PasswordEncoder implements PasswordEncoder {
    private Argon2 argon2;

    @Value("${config.argon2.iterations}")
    private int i;
    @Value("${config.argon2.memory}")
    private int mem;
    @Value("${config.argon2.parallelism}")
    private int para;

    public Argon2PasswordEncoder() {
        this(Argon2Factory.Argon2Types.ARGON2id);
    }

    public Argon2PasswordEncoder(Argon2Factory.Argon2Types type) {
        this.argon2 = Argon2Factory.create(type);
    }

    @Override
    public String encode(CharSequence password) {
        return argon2.hash(i, mem, para, password.toString().toCharArray());
    }

    @Override
    public boolean matches(CharSequence password, String hash) {
        return argon2.verify(hash, password.toString().toCharArray());
    }
}
