package com.xuyufengyy.xmh.utils;

import com.jcraft.jsch.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.Vector;

@Component
public class FtpJSch {

    private static ChannelSftp sftp = null;

    @Value(value = "${remote.server.user}")
    private String serverUser;

    @Value(value = "${remote.server.host}")
    private String serverHost;

    @Value(value = "${remote.server.password}")
    private String serverPassword;

    @Value(value = "${remote.server.port}")
    private int serverPort;

    public FtpJSch getConnect() {
        FtpJSch ftp = new FtpJSch();
        try {
            JSch jsch = new JSch();

            //获取sshSession  账号-ip-端口
            Session sshSession = jsch.getSession(serverUser, serverHost, serverPort);
            //添加密码
            sshSession.setPassword(serverPassword);
            Properties sshConfig = new Properties();
            //严格主机密钥检查
            sshConfig.put("StrictHostKeyChecking", "no");

            sshSession.setConfig(sshConfig);
            //开启sshSession链接
            sshSession.connect();
            //获取sftp通道
            Channel channel = sshSession.openChannel("sftp");
            //开启
            channel.connect();
            sftp = (ChannelSftp) channel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ftp;
    }

    /**
     * 上传
     *
     * @param fileName    文件名称
     * @param inputStream 文件流
     * @return 服务器上文件名
     */
    public String upload(String directory, String fileName, InputStream inputStream) {
        String folder = "";
        try {
            sftp.cd(directory);

            folder = DateUtils.toString(new Date(), "yyyyMMdd");
            try {
                SftpATTRS sftpATTRS = sftp.stat(folder);//目录不存在，会抛出异常
            } catch (Exception ex) {
//                ex.printStackTrace();
                sftp.mkdir(folder);
            }

            //获取随机文件名
            fileName = UUID.randomUUID().toString() + fileName.substring(fileName.length() - 5);
            //文件名是 随机数加文件名的后5位
            sftp.put(inputStream, folder + "/" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return folder + "/" + fileName;
    }

//    /**
//     * 下载文件
//     *
//     * @param directory
//     *            下载目录
//     * @param downloadFile
//     *            下载的文件名
//     * @param saveFile
//     *            存在本地的路径
//     * @param sftp
//     */
//    public void download(String directory, String downloadFileName) {
//        try {
//            sftp.cd(directory);
//
//            File file = new File(saveFile);
//
//            sftp.get(downloadFileName, new FileOutputStream(file));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 删除文件
     *
     * @param deleteFile 要删除的文件名字
     */
    public void delete(String directory, String deleteFile) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @return
     * @throws SftpException
     */
    public Vector listFiles(String directory) throws SftpException {
        return sftp.ls(directory);
    }
}
