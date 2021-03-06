package com.bbd.risinger.config;

import com.ckfinder.connector.ServletContextFactory;

import com.google.common.collect.Maps;
import com.bbd.risinger.common.utils.PropertiesLoader;
import com.bbd.risinger.common.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 全局配置类
 *
 * @author ThinkGem
 * @version 2014-06-25
 */
public class Global {

private static Logger logger = LoggerFactory.getLogger(Global.class);
	
	static RelaxedPropertyResolver resolver;

	/**
	 * 当前对象实例
	 */
	private static Global global = new Global();

	/**
	 * 保存全局属性值
	 */
	private static Map<String, String> map = Maps.newHashMap();

	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader loader = new PropertiesLoader("bootstrap.yml");

	/**
	 * 显示/隐藏
	 */
	public static final String SHOW = "1";
	public static final String HIDE = "0";

	/**
	 * 是/否
	 */
	public static final String YES = "1";
	public static final String NO = "0";

	/**
	 * 对/错
	 */
	public static final String TRUE = "true";
	public static final String FALSE = "false";

	/**
	 * 上传文件基础虚拟路径
	 */
	public static final String USERFILES_BASE_URL = "/userfiles/";

	/**
	 * 获取当前对象实例
	 */
	public static Global getInstance() {
		return global;
	}

	/**
     * 获取配置
     * ${fns:getConfig('adminPath')}
     */
    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null) {
            try {
                value = resolver.getProperty(key);
                if (StringUtils.isBlank(value)) {
					throw new RuntimeException("value null");
				}
                map.put(key, value);
            } catch (Exception e) {
                value = loader.getProperty(key);
                map.put(key, value != null ? value : StringUtils.EMPTY);
            }
        }
        return value;
    }

	/**
	 * 获取管理端根路径
	 */
	public static String getAdminPath() {
		return getConfig("adminPath");
	}

	/**
	 * 获取前端根路径
	 */
	public static String getFrontPath() {
		return getConfig("frontPath");
	}

	/**
	 * 获取URL后缀
	 */
	public static String getUrlSuffix() {
		return getConfig("urlSuffix");
	}

	/**
	 * 获取首页JSP路径
	 */
	public static String getIndexPath() {
		return getConfig("web.index.path");
	}

	/**
	 * 是否是演示模式，演示模式下不能修改用户、角色、密码、菜单、授权
	 */
	public static Boolean isDemoMode() {
		String dm = getConfig("demoMode");
		return "true".equals(dm) || "1".equals(dm);
	}
	
	/**
	 * 页面获取常量
	 * @see ${fns:getConst('YES')}
	 */
	public static Object getConst(String field) {
		try {
			return Global.class.getField(field).get(null);
		} catch (Exception e) {
			// 异常代表无配置，这里什么也不做
		}
		return null;
	}

	/**
	 * 获取工程路径
	 * @return
	 */
	public static String getProjectPath() {
		// 如果配置了工程路径，则直接返回，否则自动获取。
		String projectPath = Global.getConfig("projectPath");
		if (StringUtils.isNotBlank(projectPath)) {
			return projectPath;
		}
		try {
			File file = new DefaultResourceLoader().getResource("").getFile();
			if (file != null) {
				while (true) {
					File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
					if (f == null || f.exists()) {
						break;
					}
					if (file.getParentFile() != null) {
						file = file.getParentFile();
					} else {
						break;
					}
				}
				projectPath = file.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return projectPath;
	}

    /**
     * 获取上传文件的根目录
     *
     * @return
     */
    public static String getUserfilesBaseDir() {
        String dir = getConfig("userfiles.basedir");
        if (StringUtils.isBlank(dir)) {
            try {
                dir = ServletContextFactory.getServletContext().getRealPath("/");
            } catch (Exception e) {
                return "";
            }
        }
        if (!dir.endsWith("/")) {
            dir += "/";
        }
        return dir;
    }

    public static String getJdbcType() {
        if (map.containsKey("spring.datasource.url")) {
			return map.get("spring.datasource.url");
		}
        try {
            String url = resolver.getProperty("spring.datasource.url");
            String type = getDbType(url);
            map.put("spring.datasource.url", type);
            return type;
        } catch (Exception e) {
            logger.error("get jdbcType error", e);
        }
        logger.error("return the defaut jdbc type is mysql");
        return "mysql";
    }

    private static String getDbType(String rawUrl) {
        return rawUrl == null ? null : (!rawUrl.startsWith("jdbc:derby:") && !rawUrl.startsWith("jdbc:log4jdbc:derby:") ? (!rawUrl.startsWith("jdbc:mysql:") && !rawUrl.startsWith("jdbc:cobar:") && !rawUrl.startsWith("jdbc:log4jdbc:mysql:") ? (rawUrl.startsWith("jdbc:mariadb:") ? "mariadb" : (!rawUrl.startsWith("jdbc:oracle:") && !rawUrl.startsWith("jdbc:log4jdbc:oracle:") ? (rawUrl.startsWith("jdbc:alibaba:oracle:") ? "AliOracle" : (!rawUrl.startsWith("jdbc:microsoft:") && !rawUrl.startsWith("jdbc:log4jdbc:microsoft:") ? (!rawUrl.startsWith("jdbc:sqlserver:") && !rawUrl.startsWith("jdbc:log4jdbc:sqlserver:") ? (!rawUrl.startsWith("jdbc:sybase:Tds:") && !rawUrl.startsWith("jdbc:log4jdbc:sybase:") ? (!rawUrl.startsWith("jdbc:jtds:") && !rawUrl.startsWith("jdbc:log4jdbc:jtds:") ? (!rawUrl.startsWith("jdbc:fake:") && !rawUrl.startsWith("jdbc:mock:") ? (!rawUrl.startsWith("jdbc:postgresql:") && !rawUrl.startsWith("jdbc:log4jdbc:postgresql:") ? (rawUrl.startsWith("jdbc:edb:") ? "edb" : (!rawUrl.startsWith("jdbc:hsqldb:") && !rawUrl.startsWith("jdbc:log4jdbc:hsqldb:") ? (rawUrl.startsWith("jdbc:odps:") ? "odps" : (rawUrl.startsWith("jdbc:db2:") ? "db2" : (rawUrl.startsWith("jdbc:sqlite:") ? "sqlite" : (rawUrl.startsWith("jdbc:ingres:") ? "ingres" : (!rawUrl.startsWith("jdbc:h2:") && !rawUrl.startsWith("jdbc:log4jdbc:h2:") ? (rawUrl.startsWith("jdbc:mckoi:") ? "mckoi" : (rawUrl.startsWith("jdbc:cloudscape:") ? "cloudscape" : (!rawUrl.startsWith("jdbc:informix-sqli:") && !rawUrl.startsWith("jdbc:log4jdbc:informix-sqli:") ? (rawUrl.startsWith("jdbc:timesten:") ? "timesten" : (rawUrl.startsWith("jdbc:as400:") ? "as400" : (rawUrl.startsWith("jdbc:sapdb:") ? "sapdb" : (rawUrl.startsWith("jdbc:JSQLConnect:") ? "JSQLConnect" : (rawUrl.startsWith("jdbc:JTurbo:") ? "JTurbo" : (rawUrl.startsWith("jdbc:firebirdsql:") ? "firebirdsql" : (rawUrl.startsWith("jdbc:interbase:") ? "interbase" : (rawUrl.startsWith("jdbc:pointbase:") ? "pointbase" : (rawUrl.startsWith("jdbc:edbc:") ? "edbc" : (rawUrl.startsWith("jdbc:mimer:multi1:") ? "mimer" : (rawUrl.startsWith("jdbc:dm:") ? "dm" : (rawUrl.startsWith("jdbc:kingbase:") ? "kingbase" : (rawUrl.startsWith("jdbc:log4jdbc:") ? "log4jdbc" : (rawUrl.startsWith("jdbc:hive:") ? "hive" : (rawUrl.startsWith("jdbc:hive2:") ? "hive" : (rawUrl.startsWith("jdbc:phoenix:") ? "phoenix" : null)))))))))))))))) : "informix"))) : "h2"))))) : "hsql")) : "postgresql") : "mock") : "jtds") : "sybase") : "sqlserver") : "sqlserver")) : "oracle")) : "mysql") : "derby");
    }

}
