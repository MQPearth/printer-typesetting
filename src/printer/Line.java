package printer;

import java.util.Arrays;

/**
 * Line
 *
 * @author mqpearth
 * @date 2021.1.28 19:57
 */
public class Line {
    /**
     * 内容
     */
    private SingleLine singleLine = new SingleLine();

    /**
     * @param valueArray value内容数组
     * @param property   属性
     */
    public Line(String[] valueArray, Content.ContentProperty property) {
        //需要换多少行才能完整显示
        int maxNeedCount = 0;
        for (int i = 0; i < valueArray.length; i++) {
            //单个value的字节数组
            byte[] bytes = PrinterUtil.getBytes(valueArray[i]);
            int needCount = PrinterUtil.calMaxNeedCount(bytes.length, property.columnMaxLength[i]);
            if (needCount > maxNeedCount) {
                maxNeedCount = needCount;
            }
        }
        SingleLine lastLine = null;

        //构建单行对象
        for (int i = 0; i < maxNeedCount; i++) {
            SingleLine singleLine = new SingleLine();
            if (i + 1 != maxNeedCount) {
                singleLine.hasNextLine = true;
            }
            if (i != 0) {
                lastLine.nextLine = singleLine;
            }
            lastLine = singleLine;
            if (i == 0) {
                this.singleLine = lastLine;
            }
        }
        //填充内容
        for (int i = 0; i < valueArray.length; i++) {
            byte[][] bytes = spitArray(PrinterUtil.getBytes(valueArray[i]), property.columnMaxLength[i]);
            fill(bytes, property.columnsStartEnd[i][0]);
        }


    }

    /**
     * 填充内容
     *
     * @param bytes 可能为多行的内容
     * @param start 起点
     */
    private void fill(byte[][] bytes, int start) {
        SingleLine line = this.singleLine;
        for (byte[] singleByte : bytes) {
            append(line, singleByte, start);
            if (line.hasNextLine) {
                line = line.nextLine;
            }
        }
        null2Blank();
    }

    private void append(SingleLine line, byte[] bytes, int start) {
        System.arraycopy(bytes, 0, line.lineArray, start, bytes.length);
    }

    /**
     * 将为null的填充为空格
     */
    private void null2Blank() {
        SingleLine line = this.singleLine;
        do {
            byte[] lineArray = line.lineArray;
            for (int i = 0; i < lineArray.length; i++) {
                if (lineArray[i] == PrinterCons.DEFAULT_NULL_BYTE) {
                    lineArray[i] = PrinterCons.DEFAULT_BLANK_BYTE;
                }
            }
        } while ((line = line.nextLine) != null);
    }


    /**
     * 将内容超出长度的分割为多行
     *
     * @param bytes     内容
     * @param maxLength 最大长度
     * @return 分割后的内容
     */
    private byte[][] spitArray(byte[] bytes, int maxLength) {
        int needCount = PrinterUtil.calMaxNeedCount(bytes.length, maxLength);
        byte[][] spitArray = new byte[needCount][maxLength];
        int startIndex = 0;
        for (int i = 0; i < spitArray.length; i++) {
            spitArray[i] = Arrays.copyOfRange(bytes, startIndex, startIndex + maxLength);
            startIndex += maxLength;
        }
        return spitArray;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        SingleLine line = this.singleLine;
        do {
            builder.append(PrinterUtil.buildString(line.lineArray)).append(PrinterCons.WRAP_CHAR);
        } while ((line = line.nextLine) != null);
        return builder.toString();
    }

    /**
     * 单向链表
     */
    static class SingleLine {
        /**
         * 一行的内容
         */
        public byte[] lineArray = new byte[PrinterCons.LINE_CHAR_MAX_COUNT];

        /**
         * 是否有下一行
         */
        public boolean hasNextLine;

        /**
         * 下一行
         */
        public SingleLine nextLine;
    }
}
