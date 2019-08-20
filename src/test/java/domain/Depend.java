package domain;

public class Depend {
private String notfound;
private String group;
private String artifact;
private String version;
private int age;
public String getNotfound() {
	return notfound;
}
public void setNotfound(String notfound) {
	this.notfound = notfound;
}
public String getGroup() {
	return group;
}
public void setGroup(String group) {
	this.group = group;
}
public String getArtifact() {
	return artifact;
}
public void setArtifact(String artifact) {
	this.artifact = artifact;
}
public String getVersion() {
	return version;
}
public void setVersion(String version) {
	this.version = version;
}
public int getAge() {
	return age;
}
public void setAge(int age) {
	this.age = age;
}
@Override
public String toString() {
	return "Depend [notfound=" + notfound + ", group=" + group + ", artifact=" + artifact + ", version=" + version
			+", age=" + age + "]";
}

}
