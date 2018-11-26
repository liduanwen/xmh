package com.xuyufengyy.xmh.utils;

import java.io.*;

public class FileUpload {

    /**
     * 创建文件
     *
     * @param pathName 文件全路径
     * @return 是否创建成功，成功则返回true
     */
    public static void createFile(String pathName) {
        File file = new File(pathName);
        try {
            //如果文件不存在，则创建新的文件
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("success create file,the file is " + pathName);
                //创建文件成功后，写入内容到文件里
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 向文件中写入内容
     *
     * @param pathName 文件全路径
     * @param newstr   写入的内容
     * @return
     * @throws IOException
     */
    public static void writeFileContent(String pathName, String newstr) throws IOException {
        String filein = newstr + "\r\n";//新写入的行，换行
        String temp = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            File file = new File(pathName);//文件路径(包括文件名称)
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            //文件原有内容
            for (int i = 0; (temp = br.readLine()) != null; i++) {
                buffer.append(temp);
                // 行与行之间的分隔符 相当于“\n”
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }

    /**
     * 删除文件
     *
     * @param pathName 文件全路径
     * @return
     */
    public static void deleteFile(String pathName) {
        File file = new File(pathName);
        try {
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
