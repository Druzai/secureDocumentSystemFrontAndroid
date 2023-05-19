package ru.mirea.documenteditor.data.payload;

public class ParagraphInfo implements Comparable<ParagraphInfo> {
    private Integer number;
    private String content;
    private String align;

    public ParagraphInfo(Integer number, String content, String align) {
        this.number = number;
        this.content = content;
        this.align = align;
    }

    public ParagraphInfo(WSContent wsContent){
        this.number = wsContent.getNumber();
        this.content = wsContent.getData();;
        this.align = wsContent.getAlign();
    }

    public ParagraphInfo(ParagraphInfo paragraphInfo) {
        this.number = paragraphInfo.getNumber();
        this.content = paragraphInfo.getContent();
        this.align = paragraphInfo.getAlign();
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    @Override
    public int compareTo(ParagraphInfo o) {
        return this.number.compareTo(o.number);
    }
}
