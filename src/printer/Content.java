package printer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Content
 *
 * @author mqpearth
 * @date 2021.1.27 21:08
 */
public class Content {
    /**
     * 内容
     */
    private final LinkedList<Line> content = new LinkedList<>();

    /**
     * 属性
     */
    private final ContentProperty property;


    public Content(List<String[]> content, int[] columnMaxLength) {
        int totalCount = verify(content, columnMaxLength);
        int[][] columnsStartEnd = calProperty(columnMaxLength, totalCount);
        //内容属性
        property = new ContentProperty(columnMaxLength, columnsStartEnd);

        fillContent(content);
    }

    private void fillContent(List<String[]> lines) {
        for (String[] valueArray : lines) {
            content.add(new Line(valueArray, property));
        }
    }

    /**
     * 计算属性
     *
     * @param columnMaxLength 每个字段的最大长度
     * @param totalCount      总长度
     * @return 每列的属性
     */
    private int[][] calProperty(int[] columnMaxLength, int totalCount) {
        //列之间的平均空格 = 一行的最大长度 - 用户输入的宽度 / 空白列个数 (n-1)
        int avgBlank = (PrinterCons.LINE_CHAR_MAX_COUNT - totalCount) / (columnMaxLength.length - 1);
        int startIndex = 0;
        //计算每个列的[start, end]
        int[][] columnsStartEnd = new int[columnMaxLength.length][ContentProperty.INDEX_COUNT];
        for (int i = 0; i < columnMaxLength.length; i++) {
            int[] columnStartEnd = new int[ContentProperty.INDEX_COUNT];
            columnStartEnd[0] = startIndex;
            columnStartEnd[1] = startIndex + columnMaxLength[i];
            startIndex += columnMaxLength[i];
            columnsStartEnd[i] = columnStartEnd;
            startIndex += avgBlank;
        }
        return columnsStartEnd;
    }

    /**
     * 校验数据
     *
     * @param content         即将输出的内容
     * @param columnMaxLength 每列的最大长度
     * @return 所有列的总长度
     */
    private int verify(List<String[]> content, int[] columnMaxLength) {
        if (content.size() <= 1) {
            throw new IllegalArgumentException("content.size() <= 1");
        }
        int total = 0;
        for (int length : columnMaxLength) {
            total += length;
        }
        //字段间最少需要一个空格进行间隔
        total = total + (columnMaxLength.length - 1);
        if (total > PrinterCons.LINE_CHAR_MAX_COUNT) {
            throw new IllegalArgumentException("一行最多32位");
        }
        return total - (columnMaxLength.length - 1);
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Line line : content) {
            builder.append(line.toString()).append("--------------------------------\r\n");
        }
        return builder.toString();
    }


    public static void main(String[] args) {
        List<String[]> contentList = new ArrayList<>(3);
        contentList.add(new String[]{"count", "name", "amount"});
        contentList.add(new String[]{"1", "三利干发帽女吸水包头巾长超强浴帽速干加厚毛巾可爱擦头发干发巾", "$12.00"});
        contentList.add(new String[]{"2", "谜尚bb霜旗舰店官网迷尚大红女遮瑕持久保湿迷上粉底液隔离cc霜", "$59.00"});
        //设置各列的宽度
        Content content = new Content(contentList, new int[]{6, 18, 6});
        System.out.println(content.toString());
    }


    static class ContentProperty {
        public static final int INDEX_COUNT = 2;
        /**
         * 每一列的最大长度
         */
        public int[] columnMaxLength;
        /**
         * 每一列所允许最大的范围[start, end] start, end 为数组index
         */
        public int[][] columnsStartEnd;

        public ContentProperty(int[] columnMaxLength, int[][] columnsStartEnd) {
            this.columnMaxLength = columnMaxLength;
            this.columnsStartEnd = columnsStartEnd;
        }
    }

}
