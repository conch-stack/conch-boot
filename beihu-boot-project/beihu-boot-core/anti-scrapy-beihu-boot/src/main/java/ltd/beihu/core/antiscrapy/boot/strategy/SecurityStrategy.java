package ltd.beihu.core.antiscrapy.boot.strategy;

public interface SecurityStrategy {

    boolean checkSecurity(String target);

    boolean checkSecurity(String target1, String target2);
}
