package config;

public final class AppConfig {
    private AppConfig() {
    }

    public static String baseUrl() {
        String env = System.getenv("BASE_URL");
        if (env != null && !env.isBlank()) return env;

        String sys = System.getProperty("baseUrl");
        if (sys != null && !sys.isBlank()) return sys;

        return "https://fakerestapi.azurewebsites.net/api/v1";
    }

    public static boolean httpLoggingEnabled() {
        String v = System.getenv("HTTP_LOG");
        if (v == null) v = System.getProperty("httpLog", "false");
        return "true".equalsIgnoreCase(v);
    }
}
