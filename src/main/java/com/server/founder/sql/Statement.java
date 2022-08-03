package com.server.founder.sql;

import com.server.founder.constant.Constant;
import com.server.founder.function.Encrypt;
import com.server.founder.function.Function;
import com.server.founder.model.Role;
import com.server.founder.model.UserType;

public class Statement {
    public static String createTableVacancies="create table if not exists vacancies(\n" +
            "vacancy_id int primary key auto_increment not null,\n" +
            "title varchar(35),\n" +
            "experience_id int,\n" +
            "profession_id int,\n" +
            "vac_category_id int,\n" +
            "education_id int,\n" +
            "salary MEDIUMINT,\n" +
            "currency_id int,\n" +
            "timetable_id int,\n" +
            "user_id int,\n" +
            "description text,\n" +
            "coordinate geometry,\n" +
            "city_id int,\n" +
            "address tinytext,\n" +
            "status boolean default 1,\n" +
            "foreign key (experience_id) references vacancy_experience(experience_id)\n" +
            "on delete restrict,\n" +
            "foreign key (profession_id) references professions(profession_id)\n" +
            "on delete restrict,\n" +
            "foreign key (vac_category_id) references vacation_categories(vac_category_id)\n" +
            "on delete restrict,\n" +
            "foreign key (education_id) references education(education_id)\n" +
            "on delete restrict,\n" +
            "foreign key (currency_id) references currencies(currency_id)\n" +
            "on delete restrict,\n" +
            "foreign key (timetable_id) references timetable(timetable_id)\n" +
            "on delete restrict,\n" +
            "foreign key (user_id) references users(user_id)\n" +
            "on delete restrict,\n" +
            "foreign key (city_id) references cities(city_id)\n" +
            "on delete restrict\n" +
            ")";
    public static String createTableVacationCategories="create table if not exists vacation_categories (\n" +
            "vac_category_id int primary key auto_increment not null,\n" +
            "name varchar(25) unique,\n" +
            "index(name)\n" +
            ")";

    public static String insertCategoryOfVacancies="insert into vacation_category(name) value('Full employment'),('Part-time employment'),('Watch'),('Internship'),('Project work')";

    public static String createTableExperience="create table if not exists founder.vacancy_experience(\n" +
            "experience_id int primary key auto_increment not null,\n" +
            "name varchar(25)\n" +
            ")";

    public static String insertExperience="insert into experience(name) value('No experience'),('Irrelevant'),('From 1 year to 3 years'),('3 to 6 years'),('Over 6 years')";

    public static String createTableProfession="create table if not exists founder.profession(\n" +
            "profession_id int primary key auto_increment not null,\n" +
            "profession_name varchar(35)\n" +
            ")";
    public static String createTableEducation="create table if not exists education(\n" +
            " education_id int primary key auto_increment not null,\n" +
            " name varchar(25)\n" +
            " )";
    public static String insertEducation="insert into education(education_name) value('Basic general'),('Secondary general'),('Secondary vocational '),('Higher')";

    public static String createTableCurrencies="create table if not exists currencies(\n" +
            "currency_id int primary key auto_increment not null,\n" +
            "currency varchar(5)\n" +
            ")";
    public static String insertCurrency="insert into currencies(currency) value('RUB'),('BYN'),('CNY'),('USD'),('EUR'),('KZT')";

    public static String createTableTimetable="create table if not exists founder.timetable(\n" +
            "timetable_id int primary key auto_increment not null,\n" +
            "name varchar(25)\n" +
            ")";
    public static String insertTimeTable="insert into timetable(name) value('Fixed schedule'),('Flexible working hours')";

    public static String createTableSkills="create table if not exists skills(\n" +
            "skill_id int primary key auto_increment not null,\n" +
            "name varchar(50) not null,\n" +
            "unique(name),\n"+
            "index(name)\n" +
            ")";
    public static String createTableVacationSkills="create table if not exists founder.vacancy_skills(\n" +
            "vacancy_id int primary key auto_increment not null,\n" +
            "skill_id int not null,\n" +
            "unique(vacancy_id,skill_id),\n"+
            "foreign key (skill_id) references skills(skill_id)\n" +
            "on delete restrict,\n" +
            "foreign key (vacancy_id) references vacancies(vacancy_id)\n" +
            "on delete restrict\n" +
            ")";
    public static String createTableUserSkills="create table if not exists founder.user_skills(\n" +
            "user_id int primary key auto_increment not null,\n" +
            "skill_id int not null,\n" +
            "unique(user_id,skill_id),\n"+
            "foreign key (skill_id) references skills(skill_id)\n" +
            "on delete restrict,\n" +
            "foreign key (user_id) references vacancies(user_id)\n" +
            "on delete restrict\n" +
            ")";
    public static String createTableUserNames="create table if not exists user_names(\n" +
            "user_id int not null,\n" +
            "name varchar(50) not null,\n" +
            "unique(user_id,name)\n,"+
            "index(name),\n" +
            "foreign key (user_id) references users(user_id)\n" +
            "on delete restrict\n" +
            ")";
    public static String createTableUserProfessions="create table if not exists user_professions(\n" +
            "user_id int not null,\n" +
            "profession text not null,\n" +
            "foreign key (user_id) references users(user_id)\n" +
            "on delete restrict\n" +
            ")";
    public static String createTableUserCitizens="create table if not exists user_citizenship(\n" +
            "user_id int not null,\n" +
            "citizenship_id int not null,\n" +
            "foreign key (user_id) references users(user_id)\n" +
            "on delete restrict,\n" +
            "foreign key (citizenship_id) references citizenship(citizenship_id)\n" +
            "on delete restrict\n" +
            ")";
    public static String createTableCity="create table if not exists cities(\n" +
            "city_id int auto_increment primary key not null,\n" +
            "name varchar(20) not null\n" +
            ")";
    public static String createTableCitizenship="create table if not exists citizenship(\n" +
            "citizenship_id int auto_increment primary key not null,\n" +
            "name varchar(20) not null\n" +
            ")";
    public static String createTableProfessions="create table if not exists professions(\n" +
            "profession_id int auto_increment primary key not null,\n" +
            "name text\n" +
            ")";
    public static String createTableUserConnection="create table if not exists user_connections(\n" +
            "\tconnection_id int auto_increment primary key not null,\n" +
            "\tuser_id_1 int not null,\n" +
            "\tuser_id_2 int not null,\n" +
            "\tunique(user_id_1,user_id_2),\n" +
            "\tforeign key (user_id_1) references users(user_id)\n" +
            "\ton delete restrict,\n" +
            "\tforeign key (user_id_2) references users(user_id)\n" +
            "\ton delete restrict\n" +
            ")";
    public static String createTableChats="create table if not exists chats(\n" +
            "chat_id int primary key not null,\n" +
            "type enum('PRIVET','GROUP','CHANEL','PUBLIC')\n" +
            ")";
    public static String createTablePrivetChat="create table if not exists private_chats(\n" +
            "chat_id int unique not null,\n" +
            "user_id_1 int not null,\n" +
            "user_id_2 int not null,\n" +
            "unique(user_id_1,user_id_2),"+
            "foreign key (chat_id) references chats(chat_id) \n" +
            "on delete restrict,\n" +
            "foreign key (user_id_1) references users(user_id) \n" +
            "on delete restrict,\n" +
            "foreign key (user_id_2) references users(user_id) \n" +
            "on delete restrict\n" +
            ")";
    public static String createTablePublicChat="create table if not exists public_chats(\n" +
            "public_chat_id int auto_increment primary key not null,\n" +
            "chat_id int unique not null,\n" +
            "name text,\n" +
            "foreign key (chat_id) references chats(chat_id) \n" +
            "on delete restrict\n" +
            ")";
    public static String createTableMessages="create table if not exists messages(\n" +
            "message_id int primary key auto_increment not null,\n" +
            "chat_id int,\n" +
            "user_id int,\n" +
            "text text,\n" +
            "seen boolean default false,\n" +
            "status boolean default true,\n" +
            "date TIMESTAMP default now(),\n" +
            "index(date),\n" +
            "repost int,\n" +
            "foreign key (chat_id) references chats(chat_id) \n" +
            "on delete restrict,\n" +
            "foreign key (user_id) references users(user_id) \n" +
            "on delete restrict,\n" +
            "foreign key (repost) references messages(message_id) \n" +
            "on delete restrict\n" +
            ")";
    public static String createTableDeleteMessages="create table if not exists delete_messages(\n" +
            "message_id int,\n" +
            "user_id int,\n" +
            "unique(message_id,user_id),"+
            "foreign key (message_id) references messages(message_id) \n" +
            "on delete restrict,\n" +
            "foreign key (user_id) references users(user_id) \n" +
            "on delete restrict\n" +
            ")";
    public static String createTableUserChats="create table if not exists user_chats(\n" +
            "chat_id int,\n" +
            "user_id int,\n" +
            "unique(chat_id,user_id),"+
            "role enum('ATTACHED','USUAL','HIDDEN') default 'USUAL',\n" +
            "deleted int,\n" +
            "foreign key (user_id) references users(user_id) \n" +
            "on delete restrict,\n" +
            "foreign key (chat_id) references chats(chat_id) \n" +
            "on delete restrict,\n" +
            "foreign key (deleted) references messages(message_id) \n" +
            "on delete restrict\n" +
            ")";
    public static String createTableMessageFiles="create table if not exists message_files(\n" +
            "message_file_id int primary key auto_increment not null,"+
            "message_id int,\n" +
            "file_id int,\n" +
            "foreign key (message_id) references messages(message_id) \n" +
            "on delete restrict,\n" +
            "foreign key (file_id) references files(file_id) \n" +
            "on delete restrict\n" +
            ")";
    public static String createTableMembers="create table if not exists members(\n" +
            "member_id int primary key auto_increment not null,"+
            "public_chat_id int,\n" +
            "user_id int,\n" +
            "unique(public_chat_id,user_id),"+
            "status boolean default true,\n" +
            "role enum('admin','member'),\n" +
            "enter TIMESTAMP default now(),\n" +
            "quit TIMESTAMP,\n" +
            "foreign key (public_chat_id) references public_chats(public_chat_id) \n" +
            "on delete restrict,\n" +
            "foreign key (user_id) references users(user_id) \n" +
            "on delete restrict\n" +
            ")";
    public static String createTableFileComment="create table if not exists file_comments(\n" +
            "comment_id int primary key auto_increment not null,\n" +
            "file_id int not null,\n" +
            "user_id int not null,\n" +
            "text text,\n" +
            "date TIMESTAMP default now(),\n" +
            "foreign key (file_id) references files(file_id)\n" +
            "on delete restrict,\n" +
            "foreign key (user_id) references users(user_id)\n" +
            "on delete restrict\n" +
            ")";
    public static String createTablePostComments="create table if not exists post_comments(" +
            "   comment_id int primary key auto_increment not null,"+
            "   user_post_id int not null," +
            "   user_id int not null," +
            "   text text," +
            "   date TIMESTAMP default now()," +
            "   foreign key (user_post_id) references user_posts(user_post_id)" +
            "   on delete restrict," +
            "   foreign key (user_id) references users(user_id)" +
            "   on delete restrict" +
            ")";
    public static String createTablePostLikes = "create table if not exists post_likes(" +
            "   like_id int primary key auto_increment not null,"+
            "   user_post_id int not null," +
            "   user_id int not null," +
            "   date timestamp default now()," +
            "   index(date),"+
            "   unique(user_post_id, user_id),"+
            "   foreign key (user_post_id) references user_posts(user_post_id)" +
            "   on delete restrict," +
            "   foreign key (user_id) references users(user_id)" +
            "   on delete restrict" +
            ")";
    public static String createTableFileLikes = "create table if not exists file_likes(\n" +
            "   like_id int primary key auto_increment not null,"+
            "   file_id int not null,\n" +
            "   user_id int not null,\n" +
            "   unique(file_id, user_id),"+
            "   date timestamp default now(),\n" +
            "   index(date),\n"+
            "   foreign key (file_id) references files(file_id)\n" +
            "   on delete restrict,\n" +
            "   foreign key (user_id) references users(user_id)\n" +
            "   on delete restrict\n" +
            ")";
    public static String createTableUserAvatars="create table if not exists user_avatars(" +
            "avatar_id int primary key auto_increment not null," +
            "user_id int not null," +
            "file_id int not null," +
            "foreign key (user_id) references users(user_id)" +
            "on delete restrict," +
            "foreign key (file_id) references files(file_id)" +
            "on delete restrict" +
            ")";
    public static String createTableSubscribe="create table if not exists subscribes(" +
            "   subscribe_id int primary key auto_increment not null,"+
            "   user_id int not null," +
            "   sub_id int not null,"+
            "   unique(user_id, sub_id),"+
            "   foreign key (user_id) references users(user_id)" +
            "   on delete restrict," +
            "   foreign key (sub_id) references users(user_id)" +
            "   on delete restrict" +
            ")";
    public static String createTablePollLines="create table if not exists poll_lines(\n" +
            "\tline_id int primary key auto_increment not null,\n" +
            "\tpoll_id int not null,\n" +
            "\ttext text,\n" +
            "\tforeign key (poll_id) references polls(poll_id) \n" +
            "\ton delete restrict\n" +
            ")";
    public static String createTablePolls="create table if not exists polls(\n" +
            "\tpoll_id int primary key not null,\n" +
            "\ttype enum('SINGLE','MULTIPLE'),\n" +
            "\tview enum('USUAL','ANONYMOUS'),\n" +
            "\ttheme text,\n" +
            "\tuser_id int not null,\n" +
            "\tforeign key (user_id) references users(user_id)\n" +
            "\ton delete restrict\n" +
            ")";
    public static String createTablePosts="create table if not exists posts (\n" +
            "\tpost_id int primary key not null,\n" +
            "\ttext text,\n" +
            "\tpoll_id int," +
            "\tforeign key (poll_id) references polls(poll_id)\n" +
            "\ton delete restrict\n" +
            ")";
    public static String createTableUserPosts="create table if not exists user_posts (\n" +
            "\tuser_post_id int primary key auto_increment not null,\n" +
            "\tpost_id int not null,\n" +
            "\tpage int not null,\n" +
            "\tuser_id int not null,\n" +
            "\tstatus boolean default true,\n" +
            "\trepost int,\n" +
            "\tforeign key (post_id) references posts(post_id)\n" +
            "\ton delete restrict,\n" +
            "\tforeign key (page) references users(user_id)\n" +
            "\ton delete restrict,\n" +
            "\tdate timestamp default now(),\n" +
            "\tforeign key (repost) references user_posts(user_post_id)\n" +
            "\ton delete restrict,\n" +
            "\tforeign key (user_id) references users(user_id)\n" +
            "\ton delete restrict\n" +
            ")";
    public static String createTableLogin="create table if not exists login (" +
            "username varchar(40) primary key not null," +
            "code smallint," +
            "registration enum('PHONE','MAIL','LOGIN')"+
            ")";
    public static String createTableUsers="create table if not exists users(\n" +
            "user_id int primary key not null,\n" +
            "username varchar(45) unique  not null,\n" +
            "index(username),\n" +
            "password varchar(40) not null,\n" +
            "status boolean default true,\n" +
            "created date DEFAULT (CURRENT_DATE) not null,\n" +
            "role enum('USER','ADMIN') not null,\n" +
            "type ENUM('PERSON','COMPANY') not null,\n" +
            "registration enum('PHONE','MAIL'),\n" +
            "name varchar(50) not null,\n" +
            "confirm boolean default false,\n" +
            "birthday date,\n" +
            "gender enum('MAN', 'WOMAN'),\n" +
            "description text,\n" +
            "hobbies text,\n" +
            "qualities text,\n" +
            "city_id int,\n" +
            "foreign key (city_id) references cities(city_id)\n" +
            "on delete restrict\n" +
            ")";
    public static String createTableLineUsers="create table if not exists line_users(\n" +
            "\tvote_id int primary key auto_increment not null,\n" +
            "\tline_id int not null,\n" +
            "\tuser_id int not null,\n" +
            "\tunique(line_id, user_id),"+
            "\tforeign key (line_id) references poll_lines(line_id) \n" +
            "\ton delete cascade,\n" +
            "\tforeign key (user_id) references users(user_id) \n" +
            "\ton delete restrict\n" +
            ")\n";
    public static String createTablePostsFiles="create table if not exists post_files(" +
            "post_file_id int primary key auto_increment not null," +
            "post_id int not null," +
            "file_id int not null," +
            "foreign key (post_id) references posts(post_id)" +
            "on delete restrict," +
            "foreign key (file_id) references files(file_id)" +
            "on delete restrict" +
            ")";
    public static String createTableFiles="create table  if not exists files(" +
            "file_id int primary key auto_increment not null," +
            "url varchar(36) unique,"+
            "index(url)," +
            "contentType text," +
            "size long," +
            "status boolean default true," +
            "user_id int,"+
            "byte LONGBLOB," +
            "foreign key (user_id) references users(user_id)" +
            "on delete restrict" +
            ")";
    public static String createTablePublicAvatars="create table if not exists public_avatars(\n" +
            "avatar_id int primary key auto_increment not null,\n" +
            "chat_id int,\n" +
            "file_id int\n" +
            ")";
    public static  String createTableInterests="create table if not exists interests (\n" +
            "interest_id  int primary key auto_increment not null,\n" +
            "name varchar(36) unique\n" +
            ")";
    public static String createTableUserInterests="create table if not exists user_interests (\n" +
            "user_id int,\n" +
            "interest_id int,\n" +
            "unique(user_id,interest_id),\n" +
            "foreign key (user_id) references users(user_id)\n" +
            "on delete restrict,\n" +
            "foreign key (interest_id) references interests(interest_id)\n" +
            "on delete restrict\n" +
            ")";
    public static String createTableUserPreviewAvatar="create table if not exists user_preview_avatar(\n" +
            "user_id int unique,\n" +
            "file_id int,\n" +
            "foreign key (user_id) references users(user_id)\n" +
            "on delete restrict,\n" +
            "foreign key (file_id) references files(file_id)\n" +
            "on delete restrict\n" +
            ")";
    public static String createTableHiddenRecommendation="create table if not exists hidden_recommendation(\n" +
            "user_id_1 int,\n" +
            "user_id_2 int,\n" +
            "unique(user_id_1,user_id_2),\n" +
            "foreign key (user_id_1) references users(user_id)\n" +
            "on delete restrict,\n" +
            "foreign key (user_id_2) references users(user_id)\n" +
            "on delete restrict\n" +
            ")";
    public static String insertInterests="insert into interests(name) value('Startup'),('News'),('Investments'),('Business'),('Companies'),('Work'),('Finance'),('Networking'),('Small business'),('Service sector'),('Management'),('Trading'),('Politics'),('It'),('Real estate'),('Logistics'),('Advertising'),('Energetics'),('Design'),('Agriculture'),('Construction'),('Education'),('Psychology'),('Jurisprudence'),('Technique'),('Architecture'),('Travels'),('Art'),('Innovation'),('Science'),('Medicine'),('Music'),('Nature'),('Photo'),('Movie'),('Animals'),('Transport'),('Sports'),('Media'),('Fashion'),('Food')\n";
    public static String selectMultiplePollLines="SELECT poll_lines.line_id FROM polls\n" +
            "inner join poll_lines on (polls.poll_id=poll_lines.poll_id)\n" +
            "where polls.poll_id=? and polls.type='MULTIPLE'";
    public static String deleteAllVotes="delete line_users from line_users inner join poll_lines on (line_users.line_id=poll_lines.line_id)\n" +
            "where poll_lines.poll_id=? and line_users.user_id=?\n";
    public static String deleteVotes="delete from line_users where user_id=? and line_id in ";
    public static String selectPollLines="select poll_lines.line_id,poll_lines.text,\n" +
            "(select count(line_users.line_id) from line_users where line_users.line_id=poll_lines.line_id) as number,\n" +
            "(select count(line_users.line_id) from line_users where line_users.line_id=poll_lines.line_id and line_users.user_id=?) as my_vote\n" +
            "from (SELECT poll_lines.line_id,poll_lines.text,polls.poll_id FROM polls\n" +
            "inner join poll_lines on (polls.poll_id=poll_lines.poll_id)\n" +
            "where polls.poll_id=?) as poll_lines\n" +
            "inner join polls on(poll_lines.poll_id=polls.poll_id)\n";
    public static String one=" limit 1";
    public static String rangeLimit(Object next,int limit){
        if(next!=null) return "limit ?,"+limit;
        else return limit(limit);
    }
    public static String limit(int limit){
        return "limit "+limit;
    }


    public static String getPostLikes(boolean subscribes,Object last){
        String str = "SELECT like_id,users.user_id,users.name,user_avatar.url,users.confirm\n" +
                "FROM user_posts\n" +
                "inner join post_likes on(user_posts.user_post_id=post_likes.user_post_id)\n" +
                "inner join users on(post_likes.user_id=users.user_id)\n" +
                selectUserAvatar();
        if(subscribes) str+="inner join subscribes on(post_likes.user_id=subscribes.sub_id)\n" +
                "where subscribes.user_id=? and ";
        else str+="where ";
        str+="user_posts.user_post_id=? and user_posts.status=true\n"+
                andFindByLess(Column.like_id,last)+
                orderByDesc(Column.like_id)+
                limit(15);
        return str;
    }
    public static String findPostComments(Object last){
        return "SELECT users.user_id,users.name,user_avatar.url,users.confirm,\n" +
                "comment_id,text,date\n" +
                "from post_comments \n" +
                "inner join users on(users.user_id=post_comments.user_id) \n" +
                selectUserAvatar()+
                "where post_comments.user_post_id = ?\n" +
                andFindByLess(Column.comment_id,last)+
                "order by post_comments.comment_id desc\n"+
                limit(15);
    }
    public static String findItem(String tableName,String column,String by){
        return "SELECT "+column+" FROM "+tableName+" where "+by+"=?";
    }
    public static String selectUserAvatar(){
        return selectUserAvatar(TableName.user_avatar,Function.concat(TableName.users,Column.user_id));
    }
    public static String deleteFileFromPost="delete post_files from post_files \n" +
            "inner join user_posts on(post_files.post_id=user_posts.post_id)\n" +
            "where post_files.post_file_id=? and user_posts.user_post_id=? and user_posts.user_id=? and user_posts.repost is null";
    public static String selectUserAvatar(String tableName,String column){
        return "left join founder.files as "+tableName+" on(\n" +
                "\t(\n" +
                "\t\tSELECT file_id\n" +
                "\t\tFROM founder.user_preview_avatar\n" +
                "\t\twhere user_preview_avatar.user_id="+column+"\n" +
                "\t)="+tableName+".file_id\n" +
                ")\n";
    }
    public  static String getMutualConnection(Object last){
        return "SELECT subscribes.subscribe_id,users.user_id,users.name,users.confirm,user_avatar.url\n" +
                "FROM founder.subscribes\n" +
                "inner join subscribes as sub on (sub.sub_id=subscribes.sub_id)\n" +
                "inner join users on(subscribes.sub_id=users.user_id)\n"+
                selectUserAvatar()+
                "WHERE subscribes.user_id=? and sub.user_id=? and \n" +
                "(select count(nsub.subscribe_id) from subscribes as nsub where nsub.user_id=subscribes.sub_id and nsub.sub_id=subscribes.user_id) and\n" +
                "(select count(nsub.subscribe_id) from subscribes as nsub where nsub.user_id=sub.sub_id and nsub.sub_id=sub.user_id)\n" +
                andFindByLess(Column.subscribe_id,last)+
                orderByDesc(Function.concat(TableName.subscribes,Column.subscribe_id))+
                limit(25);
    }

    public  static String getHandshakeFirstGen(Object last){
        return "SELECT subscribes.subscribe_id,users.user_id,users.name,users.confirm, \n" +
                "user_avatar.url, exists(SELECT NSUB.user_id from founder.subscribes as NSUB where NSUB.user_id=subscribes.user_id and subscribes.sub_id=NSUB.sub_id ) AS my_sub\n" +
                "from founder.subscribes\n" +
                "INNER JOIN founder.subscribes AS sub\n" +
                "ON (sub.user_id=subscribes.sub_id AND sub.sub_id=subscribes.user_id)\n" +
                "INNER JOIN founder.users\n" +
                "ON users.user_id=subscribes.sub_id\n" +
                selectUserAvatar()+
                "WHERE founder.subscribes.user_id=? \n" +
                andFindByLess(Column.subscribe_id,last)+
                "GROUP by founder.users.user_id\n"+
                orderByDesc(Column.subscribe_id)+
                limit(25);
    }
    public  static String getHandshakeSecondGen(Object last){
        return "select subtwo.subscribe_id,users.user_id,users.name,users.confirm,user_avatar.url,\n" +
                "exists(SELECT NSUB.user_id from founder.subscribes as NSUB where NSUB.user_id=founder.subscribes.user_id and subtwo.sub_id=NSUB.sub_id ) AS my_sub\n" +
                "from subscribes\n" +
                "inner join subscribes as sub on(sub.user_id=subscribes.sub_id and sub.sub_id=subscribes.user_id)\n" +
                "inner join subscribes as subtwo on(subscribes.sub_id=subtwo.user_id)\n" +
                "inner join subscribes as subthree on(subthree.user_id=subtwo.sub_id and subthree.sub_id=subtwo.user_id)\n" +
                "inner join users on (subtwo.sub_id=users.user_id)\n"+
                selectUserAvatar()+
                "where subscribes.user_id=? and subtwo.sub_id!=subscribes.user_id\n"+
                andFindByLess(Function.concat(TableName.subtwo,Column.subscribe_id),last)+
                "GROUP by founder.users.user_id\n"+
                orderByDesc(Function.concat(TableName.subtwo,Column.subscribe_id))+
                limit(25);
    }
    public  static String getHandshakeThirdGen(Object last){
        return "select subfour.subscribe_id,users.user_id,users.name,users.confirm,user_avatar.url,exists(SELECT NSUB.user_id from founder.subscribes as NSUB where NSUB.user_id=founder.subscribes.user_id and subfour.sub_id=NSUB.sub_id ) AS my_sub\n" +
                "from subscribes\n" +
                "inner join subscribes as sub on(sub.user_id=subscribes.sub_id and sub.sub_id=subscribes.user_id)\n" +
                "inner join subscribes as subtwo on(subscribes.sub_id=subtwo.user_id)\n" +
                "inner join subscribes as subthree on(subthree.user_id=subtwo.sub_id and subthree.sub_id=subtwo.user_id)\n" +
                "inner join subscribes as subfour on(subtwo.sub_id=subfour.user_id)\n" +
                "inner join subscribes as subfive on(subfive.user_id=subfour.sub_id and subfive.sub_id=subfour.user_id)\n" +
                "inner join users on(subfour.sub_id=users.user_id)\n" +
                selectUserAvatar()+
                "where subscribes.user_id=? and subscribes.user_id!=subtwo.sub_id and subscribes.sub_id!=subfour.sub_id and subscribes.user_id!=subfour.sub_id\n" +
                andFindByLess(Function.concat(TableName.subfour,Column.subscribe_id),last)+
                "GROUP by users.user_id\n"+
                orderByDesc(Function.concat(TableName.subfour,Column.subscribe_id))+
                limit(25);
    }
    public static String setMyInterest(String params){
        return "replace into founder.user_interests (user_id,interest_id) (select ? as user_id,interests.interest_id from interests\n" +
                "where interests.interest_id in"+params+")";
    }
    public static String delMyInterest="DELETE FROM founder.user_interests \n" +
            "WHERE user_id=? and interest_id IN ";
    public static String getRelationWithUser(Object next){
        return "select user_one.user_id,user_one.name,user_one.confirm,user_one_avatar.url,\n" +
                "user_two.user_id,user_two.name,user_two.confirm,user_two_avatar.url\n" +
                "from (select ? as owner_id,? as user_id) as params\n" +
                "inner join subscribes\n" +
                "left join subscribes as sub on(subscribes.sub_id=sub.user_id and subscribes.user_id!=sub.sub_id and subscribes.sub_id!=params.user_id)\n" +
                "left join subscribes as subtwo on(sub.sub_id=subtwo.user_id and subscribes.user_id!=subtwo.sub_id and subscribes.sub_id!=subtwo.sub_id and sub.sub_id!=params.user_id)\n" +
                "inner join users as user_one on(subscribes.sub_id=user_one.user_id)\n" +
                "left join users as user_two on(if(sub.sub_id=params.user_id,null,sub.sub_id)=user_two.user_id)\n" +
                selectUserAvatar(TableName.user_one_avatar,Function.concat(TableName.user_one,Column.user_id))+
                selectUserAvatar(TableName.user_two_avatar,Function.concat(TableName.user_two,Column.user_id))+
                "where subscribes.user_id=params.owner_id and\n" +
                "(select count(nsub.user_id) from subscribes as nsub where nsub.user_id=subscribes.sub_id and nsub.sub_id=subscribes.user_id) and\n" +
                "(sub.sub_id is null or (select count(nsub.user_id) from subscribes as nsub where nsub.user_id=sub.sub_id and nsub.sub_id=sub.user_id)) and\n" +
                "(sub.sub_id is null or subtwo.sub_id is null or (select count(nsub.user_id) from subscribes as nsub where nsub.user_id=subtwo.sub_id and nsub.sub_id=subtwo.user_id))\n" +
                "and\n" +
                "(\n" +
                "\t(\n" +
                "\t\tsub.sub_id=params.user_id or \n" +
                "\t\t(\n" +
                "\t\t\tsubtwo.sub_id=params.user_id and \n" +
                "\t\t\t!(\n" +
                "\t\t\t\t(SELECT count(subscribe_id) FROM subscribes as nsub where nsub.user_id=subscribes.sub_id and nsub.sub_id=params.user_id) and\n" +
                "\t\t\t\t(SELECT count(subscribe_id) FROM subscribes as nsub where nsub.user_id=params.user_id and nsub.sub_id=subscribes.sub_id)\n" +
                "\t\t\t) and\n" +
                "\t\t\t!(\n" +
                "\t\t\t\t(SELECT count(subscribe_id) FROM subscribes as nsub where nsub.user_id=subscribes.user_id and nsub.sub_id=sub.sub_id) and \n" +
                "\t\t\t\t(SELECT count(subscribe_id) FROM subscribes as nsub where nsub.user_id=sub.sub_id and nsub.sub_id=subscribes.user_id)\n" +
                "\t\t\t)\n" +
                "\t\t)\n" +
                "\t) and \n" +
                "\t!(\n" +
                "\t\t(SELECT count(subscribe_id) FROM subscribes as nsub where nsub.user_id=subscribes.user_id and nsub.sub_id=params.user_id) and\n" +
                "\t\t(SELECT count(subscribe_id) FROM subscribes as nsub where nsub.user_id=params.user_id and nsub.sub_id=subscribes.user_id)\n" +
                "\t)\n" +
                ")\n" +
                "order by (\n" +
                "\tCASE\n" +
                "\t\tWHEN subtwo.sub_id is null THEN 2\n" +
                "\t\tELSE 3\n" +
                "\tEND\n" +
                "),subscribes.subscribe_id,sub.subscribe_id,subtwo.subscribe_id\n" +
                rangeLimit(next,15);
    }
    public static String selectPublicChatAvatar(){
        return "left join files as public_avatar on(\n" +
                "\t(\n" +
                "\t\tSELECT file_id\n" +
                "\t\tFROM public_avatars\n" +
                "\t\twhere public_avatars.chat_id=chats.chat_id\n" +
                "\t\torder by public_avatars.avatar_id desc limit 1\n" +
                "\t)=public_avatar.file_id\n" +
                ")\n";
    }
    public static String selectGen(String column){
        return "(select\n" +
                "(\n" +
                "\tCASE\n" +
                "\t\tWHEN sub.sub_id is null THEN 1\n" +
                "\t\tWHEN subtwo.sub_id is null THEN 2\n" +
                "\t\tELSE 3\n" +
                "\tEND\n" +
                ") as gen\n" +
                "from subscribes\n" +
                "left join subscribes as sub on(subscribes.sub_id=sub.user_id and subscribes.user_id!=sub.sub_id and subscribes.sub_id!="+column+")\n" +
                "left join subscribes as subtwo on(sub.sub_id=subtwo.user_id and subscribes.user_id!=subtwo.sub_id and subscribes.sub_id!=subtwo.sub_id and sub.sub_id!="+column+")\n" +
                "where subscribes.user_id=params.owner_id and\n" +
                "(select count(nsub.user_id) from subscribes as nsub where nsub.user_id=subscribes.sub_id and nsub.sub_id=subscribes.user_id) and\n" +
                "(sub.sub_id is null or (select count(nsub.user_id) from subscribes as nsub where nsub.user_id=sub.sub_id and nsub.sub_id=sub.user_id)) and\n" +
                "(sub.sub_id is null or subtwo.sub_id is null or (select count(nsub.user_id) from subscribes as nsub where nsub.user_id=subtwo.sub_id and nsub.sub_id=subtwo.user_id)) and\n" +
                "(subscribes.sub_id="+column+" or sub.sub_id="+column+" or subtwo.sub_id="+column+")\n" +
                "order by gen\n" +
                "limit 1) as gen\n";
    }
    public static String selectPageInformation="select users.user_id,users.name,user_avatar.url,users.confirm,role,registration,\n" +
            "name,description,city_id,birthday,gender,hobbies,qualities,\n" +
            "(select count(sub_id) FROM subscribes where subscribes.sub_id=users.user_id) followers,\n" +
            "(select count(sub_id) FROM subscribes where subscribes.user_id=users.user_id) subscribes,\n" +
            "(select count(user_post_id) from user_posts where user_posts.user_id=users.user_id) as post_number,\n" +
            "(select count(subscribes.sub_id) from subscribes where subscribes.user_id=params.owner_id and subscribes.sub_id=users.user_id) as my_sub,\n" +
            "(select count(subscribes.sub_id) from subscribes where subscribes.user_id=users.user_id and subscribes.sub_id=params.owner_id) as it_sub,\n" +
            selectGen(Function.concat(TableName.params,Column.user_id))+
            "from (select ? as owner_id,? as user_id) as params\n" +
            "inner join users on(params.user_id=users.user_id)\n" +
            selectUserAvatar();
    public static String bySubscribe="subscribes.sub_id=user_posts.user_id";
    public static String byUserLikes="post_likes.user_post_id=user_posts.user_post_id";
    public static String deleteUserAvatar="delete from user_avatars where avatar_id=? and user_id=?";
    public static String findUserAvatars(Object last){
        return "SELECT user_avatars.avatar_id,url,contentType,size\n" +
                "FROM user_avatars\n" +
                "inner join files on(user_avatars.file_id=files.file_id)\n"+
                findBy(Function.concat(TableName.user_avatars,Column.user_id))+
                andFindByLess(Column.avatar_id,last)+
                orderByDesc(Column.avatar_id) +
                limit(15);
    }
    public static String selectItemNumber(String tableName,String column){
        return "SELECT count("+column+") as number FROM "+tableName+" \n" +
                "where "+column+"=?";
    }
    public static String getUserLine(Object last,boolean subscribe){
        String str = "select line_users.vote_id,users.user_id,users.name,user_avatar.url,users.confirm from (\n" +
                "select polls.poll_id,poll_lines.line_id,line_users.user_id from poll_lines as poll_line\n" +
                "inner join polls on(poll_line.poll_id=polls.poll_id)\n" +
                "inner join poll_lines on(polls.poll_id=poll_lines.poll_id)\n" +
                "inner join line_users on(poll_line.line_id=line_users.line_id)\n" +
                "where poll_lines.line_id=? and polls.view='USUAL' and line_users.user_id=? limit 1) as poll\n" +
                "inner join poll_lines on(poll.poll_id=poll_lines.poll_id)\n" +
                "inner join line_users on(poll_lines.line_id=line_users.line_id)\n" +
                "inner join users on(line_users.user_id=users.user_id)"+
                selectUserAvatar();
        if(subscribe) str+="inner join subscribes on(line_users.user_id=subscribes.sub_id)\n" +
                "where subscribes.user_id=poll.user_id and ";
        else str+="where ";
        str+="line_users.line_id=poll.line_id\n"+
                andFindByLess(Column.vote_id,last)+
                orderByDesc(Column.vote_id)+
                limit(15);
        return str;
    }
    public static String getUserChats(Object next){
        return "SELECT chats.chat_id,chats.type,user_chats.role,users.user_id,users.name,user_avatar.url,users.confirm,\n" +
                "public_chats.public_chat_id,public_chats.name,public_avatar.url,\n" +
                "messages.message_id,messages.text,messages.date,\n" +
                "message_owner.user_id,message_owner.name\n" +
                "FROM user_chats\n" +
                "inner join chats on(user_chats.chat_id=chats.chat_id)\n" +
                "left join private_chats on(chats.chat_id=private_chats.chat_id)\n" +
                "left join users on(\n" +
                "if(private_chats.user_id_1=user_chats.user_id,user_id_2,user_id_1)=users.user_id)\n" +
                selectUserAvatar()+
                "left join public_chats on(chats.type!='privet' and chats.chat_id=public_chats.chat_id)\n" +
                selectPublicChatAvatar()+
                "inner join messages on(messages.message_id=(select message_id from messages where messages.chat_id=chats.chat_id\n" +
                "order by message_id desc limit 1))\n" +
                "inner join users as message_owner on(messages.user_id=message_owner.user_id)\n" +
                "where user_chats.user_id=?\n" +
                "order by user_chats.role,\n" +
                "messages.message_id desc\n"+
                rangeLimit(next,15);
    }
    public static String selectPull="SELECT polls.poll_id,polls.type,polls.view,polls.theme,\n" +
            "user_poll.user_id,user_poll.name\n" +
            "FROM polls\n" +
            "inner join users as user_poll on(polls.user_id=user_poll.user_id)\n" +
            "where polls.poll_id=?";
    public static String selectMySubOfPost="SELECT users.user_id,user_avatar.url\n" +
            "FROM user_posts\n" +
            "inner join post_likes on(user_posts.user_post_id=post_likes.user_post_id)\n" +
            "inner join users on(post_likes.user_id=users.user_id)\n" +
            selectUserAvatar()+
            "inner join subscribes on(post_likes.user_id=subscribes.sub_id)\n" +
            "where user_posts.user_post_id=? and subscribes.user_id=?\n" +
            orderByDesc(Column.like_id)+
            limit(3);
    public static String findUsersByName(int params,Object next){
        String str="select users.user_id,users.name,user_avatar.url,users.confirm,\n" +
                "(select count(subscribes.user_id) from subscribes where subscribes.user_id=params.owner_id and subscribes.sub_id=users.user_id)*3+\n" +
                "(select count(subscribes.user_id) from subscribes where subscribes.user_id=users.user_id and subscribes.sub_id=params.owner_id)*2+\n" +
                "(SELECT count(user_connections.user_id_1) FROM user_connections\n" +
                "where user_connections.user_id_1=if(users.user_id<params.owner_id,users.user_id,params.owner_id) or \n" +
                "user_connections.user_id_2=if(users.user_id>=params.owner_id,params.owner_id,users.user_id))+\n" +
                "(select count(messages.message_id) from private_chats\n" +
                "inner join messages on(private_chats.chat_id=messages.chat_id)\n" +
                "where ((private_chats.user_id_1=if(users.user_id<params.owner_id,users.user_id,params.owner_id) and private_chats.user_id_2=if(users.user_id>=params.owner_id,params.owner_id,users.user_id))) and\n" +
                "messages.date>now()-interval 1 day\n" +
                ")+\n" +
                "(select count(user_posts.user_post_id) from user_posts \n" +
                "inner join post_likes on(user_posts.user_post_id=post_likes.user_post_id)\n" +
                "where user_posts.user_id=users.user_id and post_likes.user_id=params.owner_id and post_likes.date>now()-INTERVAL 1 YEAR_MONTH\n" +
                ")*2+\n" +
                "(select count(files.file_id) from files \n" +
                "inner join file_likes on(files.file_id=file_likes.file_id)\n" +
                "where files.user_id=users.user_id and file_likes.user_id=params.owner_id and file_likes.date>now()-INTERVAL 1 YEAR_MONTH\n" +
                ")*2 as i,\n" +
                "4-"+
                selectGen(Function.concat(TableName.users,Column.user_id))+
                "from (select ? as owner_id) as params\n"+
                "inner join users\n"+
                selectUserAvatar()+
                findUsersByWords(params)+
                "group by users.user_id\n" +
                "order by i+if(gen is null,0,gen) desc,users.confirm desc,users.user_id\n" +
                rangeLimit(next,15);
        return str;
    }
    public static String findUsersByWords (int params){
        String str="";
        for (int i=0;i<params;i+=1){
            str+="inner join user_names as name_"+(i+1)+"\n";
            if(i==0) str+="on(users.user_id=name_"+(i+1)+".user_id)\n";
            else str+="on(name_"+i+".user_id=name_"+(i+1)+".user_id)\n";
        }
        if(params>0) str+="on(name_1.user_id=users.user_id)\n";
        str+="where users.user_id!=params.owner_id\n";
        for (int i=0;i<params;i+=1){
            str+="and name_"+(i+1)+".name like ?\n";
        }
        for(int i=1;i<params;i++){
            for(int j=0;j<i;j++){
                str+="and (name_"+(i+1)+".item!=name_"+(j+1)+".item)\n";
            }
        }
        return str;
    }
    public static String selectMySubOfPull="select subscribes.sub_id,user_avatar.url from polls\n" +
            "inner join poll_lines on(polls.poll_id=poll_lines.poll_id)\n" +
            "inner join line_users on(poll_lines.line_id=line_users.line_id)\n" +
            "inner join subscribes on(line_users.user_id=subscribes.sub_id)\n" +
            selectUserAvatar(TableName.user_avatar,Function.concat(TableName.subscribes,Column.sub_id))+
            "where polls.poll_id=? and subscribes.user_id=?\n" +
            "group by line_users.user_id\n" +
            "limit 3";
    public static String getPostsBy(String tableName,String by){
        String str = "SELECT user_posts.user_post_id,user_posts.post_id,\n" +
                "user_posts.date,post_comments.comment_id,\n" +
                "post_comments.text,user_comment.user_id,user_comment.name,\n" +
                "users.user_id,users.name,\n" +
                "user_avatar.url,users.confirm,\n" +
                "repost.date,\n" +
                "forward_from.user_id,forward_from.name,\n" +
                "forward_avatar.url,forward_from.confirm,\n" +
                "posts.text,\n" +
                "polls.poll_id,polls.type,polls.view,polls.theme,\n" +
                "user_poll.user_id,user_poll.name,\n" +
                "(SELECT count(post_likes.user_post_id) FROM post_likes where post_likes.user_post_id=user_posts.user_post_id) as likes,\n" +
                "(SELECT count(post_comments.user_post_id) FROM post_comments where post_comments.user_post_id=user_posts.user_post_id) as comments,\n" +
                "(SELECT count(rep.user_post_id) FROM user_posts as rep where rep.post_id=user_posts.post_id and rep.repost=user_posts.user_post_id and user_posts.status=true) as reposts,\n" +
                "(SELECT count(my_like.user_id) from post_likes as my_like where my_like.user_post_id=user_posts.user_post_id and my_like.user_id=?) as my_like\n" +
                "FROM "+tableName+" \n";
        if(by!=null) str+="inner join user_posts on("+by+") \n";
        str+="inner join posts on(user_posts.post_id=posts.post_id) \n" +
                "inner join users on(user_posts.user_id=users.user_id)\n" +
                "left join user_posts as repost on(user_posts.repost=repost.user_post_id)\n" +
                "left join users as forward_from on(repost.user_id=forward_from.user_id)\n" +
                "left join polls on (polls.poll_id=posts.poll_id)\n" +
                "left join users as user_poll on (polls.user_id=user_poll.user_id)\n"+
                "left join post_comments on (post_comments.comment_id=(SELECT max(post_comments.comment_id) from post_comments where post_comments.user_post_id=user_posts.user_post_id))\n" +
                "left join users as user_comment on(post_comments.user_id=user_comment.user_id)"+
                selectUserAvatar()+
                selectUserAvatar(TableName.forward_avatar,Function.concat(TableName.users,Column.user_id));
        return str;
    }
    public static String selectPrivetMessages(Object last){
        return "SELECT messages.message_id,messages.user_id,messages.text,messages.date,messages.repost,forward_from.user_id,forward_from.name FROM private_chats\n" +
                "inner join user_chats on(private_chats.chat_id=user_chats.chat_id)\n" +
                "inner join messages on(user_chats.chat_id=messages.chat_id)\n" +
                "left join delete_messages on(messages.message_id=delete_messages.message_id and delete_messages.user_id=user_chats.user_id)\n" +
                "left join messages as repost on(messages.repost=repost.message_id)\n" +
                "left join users as forward_from on(repost.user_id=forward_from.user_id)\n" +
                "where private_chats.user_id_1=? and private_chats.user_id_2=? and user_chats.user_id=? and \n" +
                "messages.status=true and delete_messages.message_id is null\n" +
                andFindByLess(Column.comment_id,last)+
                orderByDesc(Column.message_id)+
                limit(25);
    }
    public static String status(String tableName){
        return "and "+tableName+".status=true ";
    }
    public static String savePoll="insert into polls (poll_id,type,view,theme,user_id) values(?,?,?,?,?)";
    public static String insertIntoPollLines="insert into poll_lines(poll_id,text) values";
    public static String savePost="insert into posts (post_id,text,poll_id) values(?,?,?)";
    public static String checkPrivetChat="select users.status,user_connections.user_id_1 as user_connection,private_chats.chat_id\n" +
            "from (select ? as owner_id,? as user_id) as params\n" +
            "inner join users on(params.user_id=users.user_id)\n" +
            "left join user_connections on(user_connections.user_id_1=params.owner_id and user_connections.user_id_2=params.user_id)\n" +
            "left join private_chats on(private_chats.user_id_1=? and private_chats.user_id_2=?)";
    public static String insertIntoChats="insert into chats (chat_id,type) values(?,?)";
    public static String insertIntoPrivetChats="insert into private_chats (chat_id,user_id_1,user_id_2) values(?,?,?)";
    public static String insertIntoUserChats="insert into user_chats (chat_id,user_id) values ";
    public static String insertIntoMessages="insert into messages (chat_id,user_id,text) values(?,?,?)";
    public static String editPostPoll="UPDATE posts SET poll_id=? where post_id=?";
    public static String saveUserPosts="insert into user_posts (post_id,user_id,page) values(?,?,?)";
    public static String updatePostText="UPDATE posts SET text=? where post_id=?";
    public static String updatePostPollLines="UPDATE poll_lines SET text=? where line_id=? and poll_id=? and poll_lines.status=true";
    public static String deletePoll="UPDATE posts SET poll_id=null where post_id=?";
    public static String updatePollTheme="UPDATE polls SET theme=? where poll_id=?";
    public static String deletePostPollLines="UPDATE poll_lines SET status=false where line_id=? and poll_id=?";
    public static String deletePostFiles="delete from post_files where post_id=? and file_id in ";
    public static String repostOfPost="insert into user_posts (post_id,page,user_id,repost) values((SELECT post_id FROM user_posts as repost where repost.user_post_id=?),?,?,?)";
    public static String insertFile="insert into files (url,contentType,size,status,user_id,byte) values (?,?,?,?,?,?)";
    public static String deleteLike(String tableName,String column){
        return "delete from "+tableName+" where "+column+"=? AND user_id=?";
    }
    public static String deleteComment(String tableName){
        return "delete from "+tableName+" where comment_id=? and user_id=?";
    }
    public static String selectFileComments(Object last){
        return "SELECT users.user_id,users.name,user_avatar.url,users.confirm,\n" +
                "file_comments.comment_id,file_comments.date,file_comments.text from files\n" +
                "inner join file_comments on(files.file_id=file_comments.file_id)\n" +
                "inner join users on(file_comments.user_id=users.user_id)\n" +
                selectUserAvatar()+
                "where files.url=? and files.status=true\n" +
                andFindByLess(Column.comment_id,last)+
                orderByDesc(Function.concat(TableName.file_comments,Column.comment_id));
    }
    public static String deleteLikeOnFile="delete file_likes FROM file_likes\n" +
            "inner join files on(file_likes.file_id=files.file_id)\n" +
            "where files.url=? and file_likes.user_id=?";
    public static String getFileStatistic="SELECT users.user_id,users.name,user_avatar.url,users.confirm,\n" +
            "(select count(file_likes.like_id) from file_likes where file_likes.file_id=files.file_id) as likes,\n" +
            "(select count(file_comments.comment_id) from file_comments where file_comments.file_id=files.file_id) as comments,\n" +
            "(select 0) as reposts,\n" +
            "(select count(file_likes.like_id) from file_likes where file_likes.file_id=files.file_id and file_likes.user_id=?) as my_like\n" +
            "FROM files\n" +
            "inner join users on (files.user_id=users.user_id)\n" +
            selectUserAvatar()+
            "where files.url=? and files.status=true";
    public static String putLike(String tableName,String column){
        return "insert into "+tableName+" ("+column+",user_id) values(?,?)";
    }
    public static String getRecommendationUsers(Object next){
        return "select * from (" +
                "select users.user_id,users.name,user_avatar.url,users.confirm,\n" +
                "count(recommendation_interests.interest_id)/(select count(user_interests.interest_id) from user_interests where user_interests.user_id=recommendation_interests.user_id) as coincidence,\n" +
                "(SELECT count(subscribes.sub_id)\n" +
                "FROM founder.subscribes\n" +
                "INNER join subscribes as sub on (sub.sub_id=subscribes.sub_id)\n" +
                "WHERE subscribes.user_id=my_interests.user_id and sub.user_id=users.user_id and \n" +
                "(select count(nsub.subscribe_id) from subscribes as nsub where nsub.user_id=subscribes.sub_id and nsub.sub_id=subscribes.user_id) and\n" +
                "(select count(nsub.subscribe_id) from subscribes as nsub where nsub.user_id=sub.sub_id and nsub.sub_id=sub.user_id)\n" +
                "order by subscribes.subscribe_id desc) as mutual\n" +
                "from user_interests as my_interests\n" +
                "inner join user_interests as recommendation_interests on(my_interests.interest_id=recommendation_interests.interest_id)\n" +
                "inner join users on(recommendation_interests.user_id=users.user_id)\n" +
                selectUserAvatar()+
                "where my_interests.user_id=? and recommendation_interests.user_id!=my_interests.user_id\n" +
                "group by recommendation_interests.user_id\n" +
                "having coincidence>=0.5\n" +
                "order by coincidence desc,count(recommendation_interests.interest_id) desc,users.confirm,users.user_id\n" +
                rangeLimit(next,15)+
                ") as users\n" +
                "order by rand()\n";
    }
    public static String getPossibleUsers(Object next){
        return "select * from (" +
                "select users.user_id,users.name,users.confirm,user_avatar.url,count(users.user_id) as mutual\n" +
                "from subscribes\n" +
                "inner join subscribes as sub on(sub.user_id=subscribes.sub_id and sub.sub_id=subscribes.user_id)\n" +
                "inner join subscribes as subtwo on(subscribes.sub_id=subtwo.user_id)\n" +
                "inner join subscribes as subthree on(subthree.user_id=subtwo.sub_id and subthree.sub_id=subtwo.user_id)\n" +
                "inner join users on (subtwo.sub_id=users.user_id)\n" +
                selectUserAvatar()+
                "where subscribes.user_id=? and subtwo.sub_id!=subscribes.user_id and \n" +
                "!(SELECT count(nsub.user_id) from founder.subscribes as nsub where NSUB.user_id=founder.subscribes.user_id and subtwo.sub_id=nsub.sub_id)\n" +
                "GROUP by users.user_id\n" +
                "order by count(users.user_id) desc,users.confirm,users.user_id\n" +
                rangeLimit(next,15)+
                ") as users\n" +
                "order by rand()\n";
    }
    public static String getPopularUsers(Object next){
        return "select * from(\n" +
                "select users.user_id,users.name,users.confirm,user_avatar.url,\n" +
                "(SELECT count(subscribes.sub_id) as mutual\n" +
                "FROM founder.subscribes\n" +
                "INNER join subscribes as sub on (sub.sub_id=subscribes.sub_id)\n" +
                "WHERE subscribes.user_id=? and sub.user_id=users.user_id and \n" +
                "(select count(nsub.subscribe_id) from subscribes as nsub where nsub.user_id=subscribes.sub_id and nsub.sub_id=subscribes.user_id) and\n" +
                "(select count(nsub.subscribe_id) from subscribes as nsub where nsub.user_id=sub.sub_id and nsub.sub_id=sub.user_id)\n" +
                "order by subscribes.subscribe_id desc) as mutual\n" +
                "from subscribes\n" +
                "inner join users on(subscribes.sub_id=users.user_id)\n" +
                selectUserAvatar() +
                "where users.user_id!=? and !(select count(subscribes.subscribe_id) from subscribes as nsub where nsub.user_id=? and nsub.sub_id=subscribes.sub_id)\n" +
                "group by subscribes.sub_id\n" +
                "order by count(subscribes.sub_id) desc\n" +
                rangeLimit(next,15)+
                ") as users\n" +
                "order by rand()";
    }
    public static String checkSubscribe="SELECT users.status,(select count(subscribes.user_id) from subscribes where subscribes.user_id=? and subscribes.sub_id=users.user_id) as my_subscribe \n" +
            "FROM users\n" +
            "where user_id=?";
    public static String checkPoll="SELECT (select count(poll_lines.line_id) from poll_lines \n" +
            "where poll_lines.poll_id=polls.poll_id and poll_lines.status=true) as number \n" +
            "FROM polls\n" +
            "where poll_id=? and user_id=?";
    public static String checkPost="SELECT post_id FROM user_posts\n" +
            "where user_posts.user_post_id=? and user_posts.user_id=? and repost is null";
    public static String checkSingleVote="SELECT polls.type\n" +
            "FROM poll_lines\n" +
            "inner join polls on(polls.poll_id=poll_lines.poll_id)\n" +
            "where polls.poll_id=? and line_id=?";
    public static String checkFileComment="select file_id,status,user_id\n" +
            "from files where url=?";
    public static String getFileLikes(boolean subscribes,Object lst){
        String str="SELECT like_id,users.user_id,users.name,user_avatar.url,users.confirm FROM files\n" +
                "inner join file_likes on(files.file_id=file_likes.file_id)\n" +
                "inner join users on(file_likes.user_id=users.user_id)\n" +
                selectUserAvatar();
        if(subscribes) str+="inner join subscribes on(file_likes.user_id=subscribes.sub_id)\n" +
                "where subscribes.user_id=? and ";
        else str+="where ";
        str+="files.url=? and files.status=true\n"+
                andFindByLess(Column.like_id,lst)+
                orderByDesc(Column.like_id);
        return str;
    }
    public static String checkFileLike="select file_id,status,user_id,\n" +
            "(select count(file_likes.file_id) from file_likes where file_likes.file_id=files.file_id and file_likes.user_id=?) as my_like\n" +
            "from files where url=?";
    public static String checkPostLike="SELECT user_posts.status,user_posts.user_id,\n" +
            "(select count(post_likes.user_post_id) from post_likes where post_likes.user_post_id=user_posts.user_post_id and post_likes.user_id=?) as my_like\n" +
            "FROM user_posts where user_posts.user_post_id=?";
    public static String selectSub(String column1,String column2,boolean statistic){
        String str="SELECT users.user_id,users.name,user_avatar.url,users.confirm\n";
        if(statistic) str+=",subscribes.subscribe_id,(select count(sub.sub_id) from subscribes as sub where subscribes.sub_id=sub.sub_id and sub.user_id=?) as my_sub\n";
        str+="FROM subscribes\n" +
                "inner join users on(subscribes."+column1+"=users.user_id)\n" +
                selectUserAvatar()+
                "where subscribes."+column2+"=?\n";
        return str;
    }
    public static String selectPostFiles = "select post_files.post_file_id,files.url,files.contentType,files.size\n"+
            "from post_files\n" +
            "inner join files on(post_files.file_id=files.file_id)\n" +
            "where post_files.post_id=?\n";
    public static String saveComment(String tableName,String column){
        return "insert into "+tableName+" ("+column+",user_id,text) values(?,?,?)";
    }
    public static String insertAvatar="insert into user_avatars (user_id,file_id) values(?,?)";
    public static String insertPreviewAvatar="replace into user_preview_avatar (user_id,file_id) values(?,?)";
    public static String selectNumberOfFileFromPage="SELECT count(post_files.post_file_id) as number\n" +
            "FROM user_posts\n" +
            "inner join post_files on(user_posts.post_id=post_files.post_id)\n" +
            "inner join files on(post_files.file_id=files.file_id)\n" +
            "where user_posts.page=? and user_posts.user_id=user_posts.page and user_posts.repost is null and \n" +
            "(contentType like 'image%' or contentType like 'video%')";
    public static String selectPreviewFilesOfPage="SELECT user_posts.user_post_id,post_files.post_file_id,files.url,files.contentType,files.size\n" +
            "FROM user_posts\n" +
            "inner join post_files on(user_posts.post_id=post_files.post_id)\n" +
            "inner join files on(post_files.file_id=files.file_id)\n" +
            "where user_posts.page=? and user_posts.user_id=user_posts.page and user_posts.repost is null and \n" +
            "(contentType like 'image%' or contentType like 'video%')\n" +
            "order by post_files.post_file_id desc\n";
    public static String insertUser(UserType type){
        if(type==UserType.PERSON) return "insert into users (user_id,username,password,type,registration,name," +
                "description,city_id,birthday,gender) value(?,?,?,?,?,?,?,?,?,?)";
        else return "insert into users (user_id,username,password,role,registration,name," +
                "description,city_id) value(?,?,?,?,?,?,?,?)";
    }
    public static String insertIntoPostsFiles="insert into post_files (post_id,file_id) values ";
    public static String insertIntoLineUsers="insert into line_users (line_id,user_id) values ";
    public static String select(String column){
        return "SELECT "+column+" FROM ";
    }
    public static String tableIndex(String tableName,String column){
        return select(column)+tableName+" "+orderByDesc(column)+one;
    }
    public static String sortByLike="order by(select count(user_posts.user_post_id) from user_posts \n" +
            "inner join post_likes on(user_posts.user_post_id=post_likes.user_post_id)\n" +
            "where user_posts.user_id=subscribes.sub_id and post_likes.user_id=subscribes.user_id and post_likes.date>now()-INTERVAL 1 YEAR_MONTH\n" +
            ")+\n" +
            "(select count(files.file_id) from files \n" +
            "inner join file_likes on(files.file_id=file_likes.file_id)\n" +
            "where files.user_id=subscribes.sub_id and file_likes.user_id=subscribes.user_id and file_likes.date>now()-INTERVAL 1 YEAR_MONTH\n" +
            ") desc,users.confirm desc,subscribes.sub_id desc\n";
    public static String orderByDesc(String by){
        return "order by "+by+" desc\n";
    }
    public static String orderBy(String by){
        return "order by "+by+"\n";
    }
    public static String findBy(String by){
        return "where "+by+" = ?\n";
    }
    public static String andFindByLess(String column,Object last){
        if(last==null) return Constant.empty;
        else return "and "+column+" < ?\n";
    }
    public static String deleteFromLogin = "delete from login where username=?";
    public static String findItemBy (String tableName,String column,String by){
        return select(column)+tableName+" "+findBy(by);
    }
    public static String findTokenInformation="SELECT user_id,type FROM users where users.username=? && password=?";
    public static String subscribe="replace into "+TableName.subscribes+" (user_id,sub_id) values(?,?)";
    public static String deleteLikeOnPostPreviewFile="delete from file_likes where file_likes.file_id=(SELECT file_id from post_files \n" +
            "where post_id=(select post_id from user_posts where user_post_id=?) and post_files.status=true\n" +
            "order by post_files.file_id and post_files.status=true limit 1) and file_likes.user_id=?";
    public static String saveUserConnects="insert into user_connections(user_id_1,user_id_2) select user_id_1,user_id_2 from (select ? as user_id_1,? as user_id_2) as connection\n" +
            "where not (select count(user_connections.connection_id) from user_connections where user_connections.user_id_1=connection.user_id_1 and user_connections.user_id_2=connection.user_id_2)";
    public static String putLikeOnPostPreviewFile="insert into file_likes (file_id,user_id) select preview_file.file_id,? as user_id from (SELECT file_id from post_files \n" +
            "where post_id=(select post_id from user_posts where user_post_id=?)\n" +
            "order by post_files.file_id limit 1) as preview_file\n" +
            "where not exists (select 1 from file_likes where file_likes.file_id=preview_file.file_id and file_likes.user_id=?)";
    public static String unsubscribe="delete from "+TableName.subscribes+" where "+ Column.user_id+"=? AND "+ Column.sub_id+"=?";
    public static String replaceLogin="replace into login (username,code,registration) values(?,?,?)";
    public static String launchLoginEvent(String username){
        return "create event if not exists "+TableName.loginStatement + Encrypt.getHash(username) +
                "   on schedule at current_time + interval 3 minute" +
                "   do" +
                "       delete from login where username = '"+ username +"'"+one;
    }
    public static String dropEvent(String username){
        return "drop event if exists "+TableName.loginStatement+Encrypt.getHash(username);
    }
}
