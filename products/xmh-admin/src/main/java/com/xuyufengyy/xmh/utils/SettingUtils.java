/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.xuyufengyy.xmh.utils;

import com.google.gson.Gson;
import com.xuyufengyy.xmh.redis.Redis;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.Iterator;

/**
 * Utils - 系统设置
 *
 * @author SHOP++ Team
 * @version 3.0
 */
@Component
public class SettingUtils {

    /**
     * 日期格式配比
     */
    public static final String[] DATE_PATTERNS = new String[]{"yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss"};

    @Resource
    private Redis redis;

    private static BeanUtilsBean beanUtilsBean;

    private Gson gson = new Gson();

    static {
        ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean() {
            @Override
            public String convert(Object value) {
                if (value != null) {
                    Class<?> type = value.getClass();
                    if (type.isEnum() && super.lookup(type) == null) {
                        super.register(new EnumConverter(type), type);
                    } else if (type.isArray() && type.getComponentType().isEnum()) {
                        if (super.lookup(type) == null) {
                            ArrayConverter arrayConverter = new ArrayConverter(type, new EnumConverter(type.getComponentType()), 0);
                            arrayConverter.setOnlyFirstToString(false);
                            super.register(arrayConverter, type);
                        }
                        Converter converter = super.lookup(type);
                        return ((String) converter.convert(String.class, value));
                    }
                }
                return super.convert(value);
            }

            @SuppressWarnings("rawtypes")
            @Override
            public Object convert(String value, Class clazz) {
                if (clazz.isEnum() && super.lookup(clazz) == null) {
                    super.register(new EnumConverter(clazz), clazz);
                }
                return super.convert(value, clazz);
            }

            @SuppressWarnings("rawtypes")
            @Override
            public Object convert(String[] values, Class clazz) {
                if (clazz.isArray() && clazz.getComponentType().isEnum() && super.lookup(clazz.getComponentType()) == null) {
                    super.register(new EnumConverter(clazz.getComponentType()), clazz.getComponentType());
                }
                return super.convert(values, clazz);
            }

            @SuppressWarnings("rawtypes")
            @Override
            public Object convert(Object value, Class targetType) {
                if (super.lookup(targetType) == null) {
                    if (targetType.isEnum()) {
                        super.register(new EnumConverter(targetType), targetType);
                    } else if (targetType.isArray() && targetType.getComponentType().isEnum()) {
                        ArrayConverter arrayConverter = new ArrayConverter(targetType, new EnumConverter(targetType.getComponentType()), 0);
                        arrayConverter.setOnlyFirstToString(false);
                        super.register(arrayConverter, targetType);
                    }
                }
                return super.convert(value, targetType);
            }
        };

        DateConverter dateConverter = new DateConverter();
        dateConverter.setPatterns(DATE_PATTERNS);
        convertUtilsBean.register(dateConverter, Date.class);
        beanUtilsBean = new BeanUtilsBean(convertUtilsBean);
    }


    /**
     * 获取系统设置
     *
     * @return 系统设置
     */
    public Setting get() {
        Setting setting = new Setting();
        if (redis.isExists("shopxx", 2)) {
            setting = redis.getData("shopxx", 2, Setting.class);
        } else {
            try {
                File xmlFile = new ClassPathResource("shopxx.xml").getFile();
                Document document = new SAXReader().read(xmlFile);
                Element root = document.getRootElement();
                for (Iterator i = root.elementIterator("setting"); i.hasNext(); ) {
                    Element foo = (Element) i.next();
                    String name = foo.attributeValue("name");
                    String value = foo.attributeValue("value");
                    beanUtilsBean.setProperty(setting, name, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            redis.save(Setting.CACHE_KEY.toString(), gson.toJson(setting), 10);
        }
        return setting;
    }

    /**
     * 设置系统设置
     *
     * @param setting 系统设置
     */
    public static void set(Setting setting) {
//		try {
//			File shopxxXmlFile = new ClassPathResource("shopxx.xml").getFile();
//			Document document = new SAXReader().read(shopxxXmlFile);
//			List<Element> elements = document.selectNodes("/shopxx/setting");
//			for (Element element : elements) {
//				try {
//					String name = element.attributeValue("name");
//					String value = BeanUtils.getProperty(setting, name);
//					Attribute attribute = element.attribute("value");
//					attribute.setValue(value);
//				} catch (IllegalAccessException e) {
//					e.printStackTrace();
//				} catch (InvocationTargetException e) {
//					e.printStackTrace();
//				} catch (NoSuchMethodException e) {
//					e.printStackTrace();
//				}
//			}
//
//			FileOutputStream fileOutputStream = null;
//			XMLWriter xmlWriter = null;
//			try {
//				OutputFormat outputFormat = OutputFormat.createPrettyPrint();
//				outputFormat.setEncoding("UTF-8");
//				outputFormat.setIndent(true);
//				outputFormat.setIndent("	");
//				outputFormat.setNewlines(true);
//				fileOutputStream = new FileOutputStream(shopxxXmlFile);
//				xmlWriter = new XMLWriter(fileOutputStream, outputFormat);
//				xmlWriter.write(document);
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				if (xmlWriter != null) {
//					try {
//						xmlWriter.close();
//					} catch (IOException e) {
//					}
//				}
//				IOUtils.closeQuietly(fileOutputStream);
//			}
//
//			Ehcache cache = cacheManager.getEhcache(Setting.CACHE_NAME);
//			cache.put(new net.sf.ehcache.Element(Setting.CACHE_KEY, setting));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
    }

}