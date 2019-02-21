package ir.haveen.mivanfinal.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "data")
public class DetailsItem {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "groupId")
    private int groupId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "about")
    private String about;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "width")
    private float width;

    @ColumnInfo(name = "height")
    private float height;

    public DetailsItem(int id, int groupId, String name, String about, String image, float width, float height) {
        this.id = id;
        this.groupId = groupId;
        this.name = name;
        this.about = about;
        this.image = image;
        this.width = width;
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
