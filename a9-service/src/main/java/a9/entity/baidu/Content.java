package a9.entity.baidu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tzq0301
 * @version 1.0
 */
//@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Content {
    private Sentiment sentiment;
    private String first_vein_time;
    private String event_id;
    private String event_name;
    private List<String> vein_tag;
    private List<Vein> vein;

    @JsonProperty("vein_tag")
    private void setVein_tag(JsonNode params) {
        if (params.isArray()) {
            this.vein_tag = new ArrayList<>();
            for(JsonNode child : params) {
                this.vein_tag.add(child.asText());
            }
        } else {
            this.vein_tag = Lists.newArrayList();
        }
    }

    public void setVein_tag(List<String> vein_tag) {
        if (vein_tag == null) {
            this.vein_tag = Lists.newArrayList();
            return;
        }

        this.vein_tag = vein_tag;
    }

    public void setVein_tag(String str) {
        this.vein_tag = Lists.newArrayList();
    }

    public Sentiment getSentiment() {
        return sentiment;
    }

    public void setSentiment(Sentiment sentiment) {
        this.sentiment = sentiment;
    }

    public String getFirst_vein_time() {
        return first_vein_time;
    }

    public void setFirst_vein_time(String first_vein_time) {
        this.first_vein_time = first_vein_time;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public List<String> getVein_tag() {
        return vein_tag;
    }

    public List<Vein> getVein() {
        return vein;
    }

    public void setVein(List<Vein> vein) {
        this.vein = vein;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content content = (Content) o;
        return Objects.equal(sentiment, content.sentiment) && Objects.equal(first_vein_time, content.first_vein_time) && Objects.equal(event_id, content.event_id) && Objects.equal(event_name, content.event_name) && Objects.equal(vein_tag, content.vein_tag) && Objects.equal(vein, content.vein);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sentiment, first_vein_time, event_id, event_name, vein_tag, vein);
    }

    @Override
    public String toString() {
        return "Content{" +
                "sentiment=" + sentiment +
                ", first_vein_time='" + first_vein_time + '\'' +
                ", event_id='" + event_id + '\'' +
                ", event_name='" + event_name + '\'' +
                ", vein_tag=" + vein_tag +
                ", vein=" + vein +
                '}';
    }
}
