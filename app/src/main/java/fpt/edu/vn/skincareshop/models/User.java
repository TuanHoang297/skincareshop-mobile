package fpt.edu.vn.skincareshop.models;

public class User {

    private String _id;
    private String name;
    private String email;
    private String phone;
    private String avatar;
    private String password;
    private String skinType;

    public String getId() { return _id; }
    public void setId(String _id) { this._id = _id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getSkinType() { return skinType; }
    public void setSkinType(String skinType) { this.skinType = skinType; }

    // ====================== Login Request ======================
    public static class LoginRequest {
        private String email;
        private String password;

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // ====================== Login Response ======================
    public static class LoginResponse {
        private LoginData data;

        public LoginData getData() {
            return data;
        }

        public void setData(LoginData data) {
            this.data = data;
        }

        public static class LoginData {
            private String access_token;
            private User user;

            public String getAccessToken() {
                return access_token;
            }

            public void setAccessToken(String access_token) {
                this.access_token = access_token;
            }

            public User getUser() {
                return user;
            }

            public void setUser(User user) {
                this.user = user;
            }
        }
    }
}
