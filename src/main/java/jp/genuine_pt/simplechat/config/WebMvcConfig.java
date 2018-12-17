package jp.genuine_pt.simplechat.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.EncodedResourceResolver;
import org.springframework.web.servlet.resource.GzipResourceResolver;

/**
 * WebMvc関連の設定クラス。
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer
{
	/**
	 * Controller作るまでもないViewの設定
	 */
	@Override
	public void addViewControllers( ViewControllerRegistry registry )
	{
	}

	@Override
	public void addResourceHandlers( ResourceHandlerRegistry registry )
	{
		registry.addResourceHandler( "/webjars/**" )
				.addResourceLocations( "classpath:/META-INF/resources/webjars/" )
				.resourceChain( false ) // 自動で WebJarsResourceResolver が有効化される。
				.addResolver( new EncodedResourceResolver() ); // gz ファイルへのアクセス有効化
	}

	/**
	 * ValidationメッセージをUTF-8で設定できるようにする
	 */
	@Override
	public Validator getValidator()
	{
		return validator();
	}

	@Bean
	public LocalValidatorFactoryBean validator()
	{
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource( messageSource() );
		return bean;
	}

	@Bean
	public MessageSource messageSource()
	{
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename( "classpath:/messages" );
		messageSource.setDefaultEncoding( "UTF-8" );
		return messageSource;
	}

}
