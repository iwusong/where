package com.cn.dafeng.where.utils;

import com.cn.dafeng.where.BuildConfig;
import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


public class SendEmail {

    public static void main() throws MessagingException, GeneralSecurityException {

        Properties props = new Properties();

        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.qq.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        // 开启SSL加密，否则会失败
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);

        // 创建session
        Session session = Session.getInstance(props);

        // 创建邮件
        Message msg = new MimeMessage(session);
        // 设置标题
        msg.setSubject("通知");
        // 编辑内容
        StringBuilder builder = new StringBuilder();
        builder.append("进行定位");

        builder.append("\n时间 " + getStringDate());
        // 设置内容
        msg.setText(builder.toString());
        // 发送的邮箱地址
        msg.setFrom(new InternetAddress("2841252952@qq.com"));
        // 通过session得到transport对象
        Transport transport = session.getTransport();
        // 连接邮件服务器：邮箱类型，帐号，授权码代替密码（更安全）
        transport.connect("smtp.qq.com", "2841252952@qq.com", "sqqjqcoirphrdgcg");
        // 发送邮件
        transport.sendMessage(msg, new Address[]{new InternetAddress("1570044080@qq.com")});
        transport.close();
    }

    /**
     * 获取当前时间
     *
     * @return String
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static void SendMsg() {
        if (BuildConfig.DEBUG) {
            return;
        }
        System.out.println("发送邮件");
        Thread thread = new Thread(
                () -> {
                    try {
                        main();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }
                }
        );
        thread.start();
    }

}
