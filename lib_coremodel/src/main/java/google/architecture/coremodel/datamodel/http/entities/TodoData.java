package google.architecture.coremodel.datamodel.http.entities;

import java.util.List;

/**
 * @description: 工作待办
 * @author: HuaiAngg
 * @create: 2019-05-04 18:07
 */
public class TodoData {

    private boolean error;
    // 控制台list
    private List<ConsoleResultsBean> consoleResults;
    // 待办list
    private List<PendingResultsBean> pendingResults;
    // 通知list
    private List<NoticeResultsBean> noticeResults;

    public TodoData() {
    }

    public boolean isError() {
        return this.error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ConsoleResultsBean> getConsoleResults() {
        return consoleResults;
    }

    public void setConsoleResults(List<ConsoleResultsBean> consoleResults) {
        this.consoleResults = consoleResults;
    }

    public List<PendingResultsBean> getPendingResults() {
        return pendingResults;
    }

    public void setPendingResults(List<PendingResultsBean> pendingResults) {
        this.pendingResults = pendingResults;
    }

    public List<NoticeResultsBean> getNoticeResults() {
        return noticeResults;
    }

    public void setNoticeResults(List<NoticeResultsBean> noticeResults) {
        this.noticeResults = noticeResults;
    }

    /**
     * 控制台实体类
     */
    public static class ConsoleResultsBean {
        private int icon;
        private String name;

        public ConsoleResultsBean() {
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * 待办实体类
     */
    public static class PendingResultsBean {

        private int icon;
        private String title;
        private String author;
        private String updateTime;

        public PendingResultsBean() {
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }

    /**
     * 通知实体类
     */
    public static class NoticeResultsBean {
        private int icon;
        private String title;
        private String author;
        private String readNum;
        private String updateTime;

        public NoticeResultsBean() {
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getReadNum() {
            return readNum;
        }

        public void setReadNum(String readNum) {
            this.readNum = readNum;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
}
