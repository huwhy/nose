package cn.huwhy.nose;

import java.io.File;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;

import cn.huwhy.nose.biz.ItemBiz;
import cn.huwhy.nose.dao.DaoConfig;
import cn.huwhy.nose.dao.MyBatisMapperScannerConfig;
import cn.huwhy.nose.task.TaskConfig;

@Configuration
@ComponentScan(basePackageClasses = {ItemBiz.class})
@Import({TaskConfig.class, DaoConfig.class, MyBatisMapperScannerConfig.class})
public class AppConfig {

    @Bean
    public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
        FileSystemResource[] locations = getFileSystemResources();
        configurer.setLocations(locations);
        return configurer;
    }

    private FileSystemResource[] getFileSystemResources() {
        String confPath = System.getProperties().getProperty("conf.path");
        if (confPath == null) {
            confPath = "/data/nose/conf";
        }
        File file = new File(confPath);
        String[] props = file.list((dir, name) -> name.endsWith(".properties"));
        FileSystemResource[] locations = new FileSystemResource[props.length];
        for (int i = 0; i < props.length; i++) {
            locations[i] = new FileSystemResource(confPath + File.separator + props[i]);
        }
        return locations;
    }
}
