package com.androidfactorem.jasonisframed;

public class Jason {
  private String id;
  private String atype;
  private String atext;
  private String aimage;
  private String alink;
  private String avalid;

  public String getid() { return id; }
  public String gettype() { return atype; }
  public String gettext() { return atext; }
  public String getimage() { return aimage; }
  public String getLink() { return alink; }
  public String getavalid() { return avalid; }

  public Jason(String _id, String _atype, String _atext, String _aimage, String _alink, String _avalid) {
    id = _id;
    atype = _atype;
    atext = _atext;
    aimage = _aimage;
    alink = _alink;
    avalid = _avalid;
  }

}
