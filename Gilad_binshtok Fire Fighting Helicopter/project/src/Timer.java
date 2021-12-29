public class Timer {
        private long lastTimeUpdated;
        public long delay;

    public Timer(long delay)    {
        this.delay=delay;
    }
    public long getLastTimeUpdated() {
        return lastTimeUpdated;
    }
    public void setLastTimeUpdated(long lastTimeUpdated) {
        this.lastTimeUpdated = lastTimeUpdated;
    }
            public boolean isTimeToUpdate()
    {
        if (System.currentTimeMillis() - this.lastTimeUpdated
                 >delay) // הגיע הזמן לעשות משהו
              return true;
        return false;
    }
    public void setLastTimeUpdated(){
        this.lastTimeUpdated = System.currentTimeMillis();
    }
}
