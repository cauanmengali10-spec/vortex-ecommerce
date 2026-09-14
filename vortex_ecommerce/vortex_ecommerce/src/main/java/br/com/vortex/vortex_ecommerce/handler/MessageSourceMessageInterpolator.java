package br.com.vortex.vortex_ecommerce.handler;

import java.util.Locale;

import jakarta.validation.MessageInterpolator;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessageSourceMessageInterpolator implements MessageInterpolator {

  private final MessageSource messageSource;
  private final MessageInterpolator fallback = new ResourceBundleMessageInterpolator();

  public MessageSourceMessageInterpolator(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public String interpolate(String messageTemplate, Context context) {
    return interpolate(messageTemplate, context, LocaleContextHolder.getLocale());
  }

  @Override
  public String interpolate(String messageTemplate, Context context, Locale locale) {
    if (messageTemplate == null || !isMessageKey(messageTemplate)) {
      return fallback.interpolate(messageTemplate, context, locale);
    }

    String key = messageTemplate.substring(1, messageTemplate.length() - 1);
    String resolved;
    try {
      resolved = messageSource.getMessage(key, null, locale);
    } catch (NoSuchMessageException ex) {
      return fallback.interpolate(messageTemplate, context, locale);
    }

    return fallback.interpolate(resolved, context, locale);
  }

  private boolean isMessageKey(String messageTemplate) {
    return messageTemplate.startsWith("{") && messageTemplate.endsWith("}");
  }
}
