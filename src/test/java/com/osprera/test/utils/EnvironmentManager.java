package com.osprera.test.utils;

/**
 * Clase para manejar múltiples ambientes de testing
 * Permite cambiar fácilmente entre producción, testing y desarrollo
 */
public class EnvironmentManager {
    
    /**
     * Obtiene la URL del ambiente actual
     */
    public static String getCurrentUrl() {
        String ambiente = Vars.get("ambiente");
        if (ambiente == null) ambiente = "produccion";
        return Vars.get(ambiente + ".url");
    }
    
    /**
     * Obtiene el usuario del ambiente actual para pedidos
     */
    public static String getCurrentUsuario() {
        String ambiente = Vars.get("ambiente");
        if (ambiente == null) ambiente = "produccion";
        return Vars.get(ambiente + ".usuario");
    }
    
    /**
     * Obtiene el usuario específico para pedidos
     */
    public static String getUsuarioPedidos() {
        String ambiente = Vars.get("ambiente");
        if (ambiente == null) ambiente = "produccion";
        if ("qa".equals(ambiente)) {
            return Vars.get("qa.usuario.pedidos");
        } else if ("produccion".equals(ambiente)) {
            return Vars.get("produccion.usuario.pedidos");
        }
        return Vars.get(ambiente + ".usuario");
    }
    
    /**
     * Obtiene el usuario específico para auditoría provincial
     */
    public static String getUsuarioAuditoriaProvincial() {
        String ambiente = Vars.get("ambiente");
        if (ambiente == null) ambiente = "produccion";
        if ("qa".equals(ambiente)) {
            return Vars.get("qa.usuario.auditoria_provincial");
        } else if ("produccion".equals(ambiente)) {
            return Vars.get("produccion.usuario.auditoria_provincial");
        }
        return Vars.get(ambiente + ".usuario");
    }
    
    /**
     * Obtiene el usuario específico para auditoría central
     */
    public static String getUsuarioAuditoriaCentral() {
        String ambiente = Vars.get("ambiente");
        if (ambiente == null) ambiente = "produccion";
        if ("qa".equals(ambiente)) {
            return Vars.get("qa.usuario.auditoria_central");
        } else if ("produccion".equals(ambiente)) {
            return Vars.get("produccion.usuario.auditoria_central");
        }
        return Vars.get(ambiente + ".usuario");
    }
    
    /**
     * Obtiene la contraseña del ambiente actual
     */
    public static String getCurrentPassword() {
        String ambiente = Vars.get("ambiente");
        if (ambiente == null) ambiente = "produccion";
        return Vars.get(ambiente + ".password");
    }
    
    /**
     * Obtiene el tipo de usuario del ambiente actual
     */
    public static String getCurrentTipoUsuario() {
        String ambiente = Vars.get("ambiente");
        if (ambiente == null) ambiente = "produccion";
        return Vars.get(ambiente + ".tipo_usuario");
    }
    
    /**
     * Obtiene el nombre del ambiente actual
     */
    public static String getCurrentEnvironment() {
        String ambiente = Vars.get("ambiente");
        if (ambiente == null) ambiente = "produccion";
        return ambiente;
    }
    
    /**
     * Cambia el ambiente (útil para tests que necesiten cambiar de ambiente)
     * Nota: Esto solo cambia en memoria, no en el archivo properties
     */
    public static void setEnvironment(String newEnvironment) {
        // Nota: Vars no permite cambiar propiedades dinámicamente
        // Para cambiar ambiente, editar default.properties
        System.out.println("⚠️ Para cambiar ambiente, editar: src/test/resources/testdata/default.properties");
        System.out.println("🌍 Ambiente actual: " + getCurrentEnvironment());
    }
    
    /**
     * Obtiene información del ambiente actual para logging
     */
    public static String getEnvironmentInfo() {
        String ambiente = getCurrentEnvironment();
        String url = getCurrentUrl();
        String usuario = getCurrentUsuario();
        
        return String.format("🌍 Ambiente: %s | 🔗 URL: %s | 👤 Usuario: %s", 
                           ambiente, url, usuario);
    }
    
    /**
     * Valida que el ambiente actual tenga todas las propiedades necesarias
     */
    public static boolean isEnvironmentValid() {
        String ambiente = getCurrentEnvironment();
        String url = getCurrentUrl();
        String usuario = getCurrentUsuario();
        String password = getCurrentPassword();
        
        return url != null && !url.isEmpty() && 
               usuario != null && !usuario.isEmpty() && 
               password != null && !password.isEmpty();
    }
}
