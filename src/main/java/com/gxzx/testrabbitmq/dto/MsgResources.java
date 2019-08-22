package com.gxzx.testrabbitmq.dto;



import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;


/**
 * 加载配置
 * 
 */
public final class MsgResources {
    /** 国际化信息 */
    private static final Map<String, ResourceBundle> MESSAGES = new HashMap<String, ResourceBundle>();

    /** 国际化信息 */
    public static String getMessage(String key, Object... params) {
    	Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle message = MESSAGES.get(locale.getLanguage());
        if (message == null) {
            synchronized (MESSAGES) {
                message = MESSAGES.get(locale.getLanguage());
                if (message == null) {
                    message = ResourceBundle.getBundle("properties/i18n/messages", locale);
                    MESSAGES.put(locale.getLanguage(), message);
                }
            }
        }
        String keyValue = null;
        if (params != null && params.length > 0) {
        	try {
        		keyValue = message.getString(key);
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}
        	if(keyValue != null && StringUtils.isNotBlank(keyValue)){
        		return String.format(keyValue, params);
        	}else{
        		return null;
        	}
            
        }
    	try {
    		keyValue = message.getString(key);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
    	if(keyValue != null && StringUtils.isNotBlank(keyValue)){
    		return message.getString(key);
    	}else{
    		return null;
    	}        
    }

    /** 清除国际化信息 */
    public static void flushMessage() {
        MESSAGES.clear();
    }
    
}
