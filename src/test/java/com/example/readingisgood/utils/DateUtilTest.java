//package com.example.readingisgood.utils;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.Month;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.time.format.FormatStyle;
//import java.time.temporal.TemporalAccessor;
//import java.util.Locale;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//class DateUtilTest {
//
//    @InjectMocks
//    private DateUtil dateUtil;
//
//    @Test
//    void it_should_get_datetimeformatter(){
//
//        String formattedDate = "2021 Dec 7 22:22:31 TRT";
//        TemporalAccessor temporalAccessor = LocalDateTime.of(2021, Month.DECEMBER,7,22,22);
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)
//                .withLocale(new Locale("tr-TR"))
//                .withZone(ZoneId.systemDefault());
//
//        given(dateUtil.getDateTimeFormatter()).willReturn(dateTimeFormatter); // .format(temporalAccessor)
//
//        String fd = dateUtil.getDateTimeFormatter().format(temporalAccessor);
//
//        assertThat(fd).isEqualTo(formattedDate);
//
//    }
//
//}