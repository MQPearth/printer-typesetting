# printer-typesetting

printer-typesetting / 打印排版

部分小票打印机没有排版方法, 所以有了这个项目


示例

```
  List<String[]> contentList = new ArrayList<>(3);
  contentList.add(new String[]{"count", "amount", "name"});
  contentList.add(new String[]{"namenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamename", "namenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamename", "namenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamename"});
  contentList.add(new String[]{"namenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamename", "namenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamename", "namenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamename"});
  //设置各列的宽度
  Content content = new Content(contentList, new int[]{10, 10, 10});
  System.out.println(content.toString());
```

效果

![](doc/demo.png)