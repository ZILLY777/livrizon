package com.server.founder.sql;

import com.server.founder.constant.Constant;
import com.server.founder.function.Encrypt;
import com.server.founder.function.Function;

public class Statement {
    public static String createTableUserConnection="create table if not exists user_connection(\n" +
            "\tconnection_id int auto_increment primary key not null,\n" +
            "\tuser_id_1 int not null,\n" +
            "\tindex(user_id_1),\n" +
            "\tuser_id_2 int,\n" +
            "\tindex(user_id_2),\n" +
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
    public static String createTablePrivetChat="create table if not exists privet_chats(\n" +
            "chat_id int unique not null,\n" +
            "index(chat_id),\n" +
            "user_id_1 int not null,\n" +
            "index(user_id_1),\n" +
            "user_id_2 int not null,\n" +
            "index(user_id_2),\n" +
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
            "index(chat_id),\n" +
            "name text,\n" +
            "foreign key (chat_id) references chats(chat_id) \n" +
            "on delete restrict\n" +
            ")";
    public static String createTableMessages="create table if not exists messages(\n" +
            "message_id int primary key auto_increment not null,\n" +
            "chat_id int,\n" +
            "index(chat_id),\n" +
            "user_id int,\n" +
            "text text,\n" +
            "seen boolean default false,\n" +
            "status boolean default true,\n" +
            "date TIMESTAMP default now(),\n" +
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
            "index(message_id),\n" +
            "user_id int,\n" +
            "index(user_id),\n" +
            "unique(message_id,user_id),"+
            "foreign key (message_id) references messages(message_id) \n" +
            "on delete restrict,\n" +
            "foreign key (user_id) references users(user_id) \n" +
            "on delete restrict\n" +
            ")";
    public static String createTableUserChats="create table if not exists user_chats(\n" +
            "chat_id int,\n" +
            "index(chat_id),\n" +
            "user_id int,\n" +
            "index(user_id),\n" +
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
            "index(file_id),\n" +
            "user_id int not null,\n" +
            "index(user_id),\n" +
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
            "   index(user_post_id)," +
            "   user_id int not null," +
            "   index(user_id)," +
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
            "   index(user_post_id)," +
            "   user_id int not null," +
            "   index(user_id),"+
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
            "   index(file_id),\n" +
            "   user_id int not null,\n" +
            "   index(user_id),\n" +
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
            "index(user_id)," +
            "file_id int not null," +
            "foreign key (user_id) references users(user_id)" +
            "on delete restrict," +
            "foreign key (file_id) references files(file_id)" +
            "on delete restrict" +
            ")";
    public static String createTableSubscribe="create table if not exists subscribes(" +
            "   subscribe_id int primary key auto_increment not null,"+
            "   user_id int not null," +
            "   index(user_id),"+
            "   sub_id int not null,"+
            "   index(sub_id),"+
            "   unique(user_id, sub_id),"+
            "   foreign key (user_id) references users(user_id)" +
            "   on delete restrict," +
            "   foreign key (sub_id) references users(user_id)" +
            "   on delete restrict" +
            ")";
    public static String createTablePollLines="create table if not exists poll_lines(\n" +
            "\tline_id int primary key auto_increment not null,\n" +
            "\tpoll_id int not null,\n" +
            "\tindex(poll_id),\n" +
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
            "\tindex(post_id),\n" +
            "\tpage int not null,\n" +
            "\tindex(page),\n" +
            "\tuser_id int not null,\n" +
            "\tindex(user_id),\n" +
            "\tstatus boolean default true,\n" +
            "\trepost int,\n" +
            "\tindex(repost),\n" +
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
            "   username varchar(40) primary key not null," +
            "   code smallint," +
            "   registration enum('PHONE','MAIL','LOGIN')"+
            ")";
    public static String createTableUsers="create table if not exists users(" +
            "   user_id int primary key not null," +
            "   username varchar(45) unique  not null," +
            "   index(username)," +
            "   password varchar(40)," +
            "   status boolean default true," +
            "   created datetime default now()," +
            "   role enum('USER','ADMIN','CHANNEL','INFORMATION','SUPPORT') default 'USER'," +
            "   registration enum('PHONE','MAIL','LOGIN')," +
            "   first_name varchar(20)," +
            "   last_name varchar(20)," +
            "   confirm boolean default false,"+
            "   birthday datetime,"+
            "   city text,"+
            "   description text,"+
            "   company text,"+
            "   education text"+
            ")";
    public static String createTableLineUsers="create table if not exists line_users(\n" +
            "\tvote_id int primary key auto_increment not null,\n" +
            "\tline_id int not null,\n" +
            "\tindex(line_id),"+
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
            "index(post_id)," +
            "file_id int not null," +
            "foreign key (post_id) references posts(post_id)" +
            "on delete restrict," +
            "foreign key (file_id) references files(file_id)" +
            "on delete restrict" +
            ")";
    public static String createTableFiles="create table  if not exists files(" +
            "   file_id int primary key auto_increment not null," +
            "   url varchar(36) unique,"+
            "   index(url)," +
            "   contentType text," +
            "   size long," +
            "   status boolean default true," +
            "   user_id int,"+
            "   byte LONGBLOB," +
            "   foreign key (user_id) references users(user_id)" +
            "   on delete restrict" +
            ")";
    public static String createTablePublicAvatars="create table if not exists public_avatars(\n" +
            "avatar_id int primary key auto_increment not null,\n" +
            "chat_id int,\n" +
            "file_id int\n" +
            ")";
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
        String str = "SELECT like_id,users.user_id,users.first_name,users.last_name,user_avatar.url,users.confirm\n" +
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
        return "SELECT users.user_id,users.first_name,users.last_name,user_avatar.url,users.confirm,\n" +
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
                "\t\tFROM founder.user_avatars\n" +
                "\t\twhere user_avatars.user_id="+column+"\n" +
                "\t\torder by user_avatars.avatar_id desc limit 1\n" +
                "\t)="+tableName+".file_id\n" +
                ")\n";
    }
    public  static String getSubOfSub(Object last){
        return "SELECT subscribe_id,users.user_id,first_name, last_name,user_avatar.url,users.confirm\n" +
                "FROM founder.subscribes\n" +
                "INNER join(SELECT sub_id FROM founder.subscribes WHERE user_id=?) AS SUB\n" +
                "ON SUB.sub_id=founder.subscribes.sub_id\n" +
                "INNER JOIN founder.users\n" +
                "ON users.user_id = subscribes.sub_id\n" +
                selectUserAvatar()+
                "WHERE founder.subscribes.user_id=?\n"+
                andFindByLess(Column.subscribe_id,last)+
                orderByDesc(Column.subscribe_id)+
                limit(25);
    }

    public  static String getHandshakeFirstGen(Object last){
        return "SELECT founder.users.user_id, founder.users.first_name, founder.users.last_name, founder.users.confirm, subscribes.subscribe_id,user_avatar.url,  exists(SELECT * from founder.subscribes where founder.subscribes.user_id=3 and founder.subscribes.sub_id=sub.sub_id ) AS sub_status\n" +
                "from founder.subscribes\n" +
                "INNER JOIN founder.subscribes AS sub\n" +
                "ON sub.user_id=founder.subscribes.sub_id\n" +
                "INNER JOIN founder.users\n" +
                "ON founder.users.user_id=sub.sub_id\n" +
                selectUserAvatar()+
                "WHERE founder.subscribes.user_id=? AND sub.sub_id!=subscribes.user_id\n" +
                "GROUP by founder.users.user_id\n" +
                ///"ORDER BY users.user_id\n" +
                ///"WHERE founder.subscribes.user_id=?\n"+
                andFindByLess(Column.subscribe_id,last)+
                orderByDesc(Column.subscribe_id)+
                limit(25);
    }
    public  static String getHandshakeSecondGen(Object last){
        return "SELECT founder.users.user_id, founder.users.first_name, founder.users.last_name, founder.users.confirm, subscribes.subscribe_id,user_avatar.url\n" +
                "from founder.subscribes\n" +
                "INNER JOIN founder.subscribes AS sub\n" +
                "ON sub.user_id=founder.subscribes.sub_id\n" +
                "INNER JOIN founder.users\n" +
                "ON founder.users.user_id=sub.sub_id\n" +
                selectUserAvatar()+
                "WHERE founder.subscribes.user_id=? AND sub.sub_id!=subscribes.user_id\n" +
                "GROUP by founder.users.user_id\n" +
                ///"ORDER BY users.user_id\n" +
                ///"WHERE founder.subscribes.user_id=?\n"+
                andFindByLess(Column.subscribe_id,last)+
                orderByDesc(Column.subscribe_id)+
                limit(25);
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
    public static String selectUserInformation="SELECT users.user_id,first_name,last_name,user_avatar.url,users.confirm,birthday,city,company,education,\n" +
            "(SELECT count(sub_id) FROM subscribes where subscribes.sub_id=users.user_id) followers,\n" +
            "(SELECT count(sub_id) FROM subscribes where subscribes.user_id=users.user_id) subscribes,\n" +
            "(SELECT count(post_files.post_file_id) as number\n" +
            "FROM user_posts\n" +
            "inner join post_files on(user_posts.post_id=post_files.post_id)\n" +
            "inner join files on(post_files.file_id=files.file_id)\n" +
            "where user_posts.page=users.user_id and user_posts.user_id=user_posts.page and user_posts.repost is null and \n" +
            "(contentType like 'image%' or contentType like 'video%')\n" +
            ") as files_number\n" +
            "FROM users\n" +
            selectUserAvatar()+
            "where users.user_id = ?\n" +
            "\n";
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
        String str = "select line_users.vote_id,users.user_id,users.first_name,users.last_name,user_avatar.url,users.confirm from (\n" +
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
        return "SELECT chats.chat_id,chats.type,user_chats.role,users.user_id,users.first_name,users.last_name,user_avatar.url,users.confirm,\n" +
                "public_chats.public_chat_id,public_chats.name,public_avatar.url,\n" +
                "messages.message_id,messages.text,messages.date,\n" +
                "message_owner.user_id,message_owner.first_name,message_owner.last_name\n" +
                "FROM user_chats\n" +
                "inner join chats on(user_chats.chat_id=chats.chat_id)\n" +
                "left join privet_chats on(chats.chat_id=privet_chats.chat_id)\n" +
                "left join users on(\n" +
                "if(privet_chats.user_id_1=user_chats.user_id,user_id_2,user_id_1)=users.user_id)\n" +
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
            "user_poll.user_id,user_poll.first_name,user_poll.last_name\n" +
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
                "post_comments.text,user_comment.user_id,user_comment.first_name,user_comment.last_name,\n" +
                "users.user_id,users.first_name,users.last_name,\n" +
                "user_avatar.url,users.confirm,\n" +
                "repost.date,\n" +
                "forward_from.user_id,forward_from.first_name,forward_from.last_name,\n" +
                "forward_avatar.url,forward_from.confirm,\n" +
                "posts.text,\n" +
                "polls.poll_id,polls.type,polls.view,polls.theme,\n" +
                "user_poll.user_id,user_poll.first_name,user_poll.last_name,\n" +
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
        return "SELECT messages.message_id,messages.user_id,messages.text,messages.date,messages.repost,forward_from.user_id,forward_from.first_name,forward_from.last_name FROM privet_chats\n" +
                "inner join user_chats on(privet_chats.chat_id=user_chats.chat_id)\n" +
                "inner join messages on(user_chats.chat_id=messages.chat_id)\n" +
                "left join delete_messages on(messages.message_id=delete_messages.message_id and delete_messages.user_id=user_chats.user_id)\n" +
                "left join messages as repost on(messages.repost=repost.message_id)\n" +
                "left join users as forward_from on(repost.user_id=forward_from.user_id)\n" +
                "where privet_chats.user_id_1=? and privet_chats.user_id_2=? and user_chats.user_id=? and \n" +
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
    public static String selectChatIdFromPrivetChat="SELECT chat_id FROM privet_chats\n" +
            "where user_id_1=? and user_id_2=?";
    public static String checkPrivetChat="select users.status,privet_chats.chat_id from users\n" +
            "left join privet_chats on(user_id_1=? and user_id_2=?)\n" +
            "where user_id=?";
    public static String insertIntoChats="insert into chats (chat_id,type) values(?,?)";
    public static String insertIntoPrivetChats="insert into privet_chats (chat_id,user_id_1,user_id_2) values(?,?,?)";
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
    public static String findItemByTwoParam(String tableName,String column1,String colum2){
        return select(column1+","+colum2)+tableName+" where "+column1+"=? && "+colum2+"=?"+one;
    }
    public static String deleteLike(String tableName,String column){
        return "delete from "+tableName+" where "+column+"=? AND user_id=?";
    }
    public static String deleteComment(String tableName){
        return "delete from "+tableName+" where comment_id=? and user_id=?";
    }
    public static String selectFileComments(Object last){
        return "SELECT users.user_id,users.first_name,users.last_name,user_avatar.url,users.confirm,\n" +
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
    public static String getFileStatistic="SELECT users.user_id,users.first_name,users.last_name,user_avatar.url,users.confirm,\n" +
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
        String str="SELECT like_id,users.user_id,users.first_name,users.last_name,user_avatar.url,users.confirm FROM files\n" +
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
    public static String selectSub(String column1,String column2){
        return "SELECT subscribes.subscribe_id,users.user_id,users.first_name,users.last_name,user_avatar.url,users.confirm FROM subscribes\n" +
                "inner join users on(subscribes."+column1+"=users.user_id)\n" +
                selectUserAvatar()+
                "where subscribes."+column2+"=?\n";
    }
    public static String selectPostFiles = "select post_files.post_file_id,files.url,files.contentType,files.size\n"+
            "from post_files \n" +
            "inner join files on(post_files.file_id=files.file_id) \n" +
            "where post_files.post_id=? \n";
    public static String saveComment(String tableName,String column){
        return "insert into "+tableName+" ("+column+",user_id,text) values(?,?,?)";
    }
    public static String insertAvatar="insert into user_avatars (user_id,file_id) values(?,?)";
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
    public static String insertUser="insert into users (user_id,username,password,registration,first_name,last_name) value(?,?,?,?,?,?)";
    public static String insertIntoPostsFiles="insert into post_files (post_id,file_id) values ";
    public static String insertIntoLineUsers="insert into line_users (line_id,user_id) values ";
    public static String findUsersByName(Object user_id,String[] listOfName,Object next){
        return "SELECT users.user_id,users.first_name,users.last_name,user_avatar.url,users.confirm\n" +
                "FROM users\n" +
                "left join user_connection on((user_connection.user_id_1=users.user_id || user_connection.user_id_2=users.user_id) and\n" +
                "(user_connection.user_id_1=? or user_connection.user_id_2=?))\n" +
                "left join subscribes on(users.user_id=subscribes.sub_id and subscribes.user_id=?)\n" +
                selectUserAvatar() +
                "where "+
                switch (listOfName.length){
                    case 1-> findUserByLastNameOrFirstName;
                    default -> findUserByLastNameAndFirstName;
                }+
                switch (user_id){
                    case null -> "";
                    default -> "and users.user_id!=?\n";
                } +
                "order by subscribes.user_id is not null desc,\n" +
                "connection_id is not null desc,\n" +
                "users.user_id in (SELECT subscribesOfSub.sub_id FROM subscribes\n" +
                "left join subscribes as subscribesOfSub on(subscribes.sub_id=subscribesOfSub.user_id)\n" +
                "where subscribes.user_id=?) desc,\n" +
                "users.confirm desc,\n"+
                "user_id\n"+
                rangeLimit(next,15);
    }
    public static String findUserByLastNameAndFirstName="((last_name like ? && first_name like ?) || (first_name like ? && last_name like ?))\n";
    public static String findUserByLastNameOrFirstName="(last_name like ? || first_name like ?)\n";
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
    public static String subscribe="replace into "+TableName.subscribes+" (user_id,sub_id) values(?,?)";
    public static String deleteLikeOnPostPreviewFile="delete from file_likes where file_likes.file_id=(SELECT file_id from post_files \n" +
            "where post_id=(select post_id from user_posts where user_post_id=?) and post_files.status=true\n" +
            "order by post_files.file_id and post_files.status=true limit 1) and file_likes.user_id=?";
    public static String saveUserConnection="insert into user_connection(user_id_1,user_id_2) select user_id_1,user_id_2 from (select ? as user_id_1,? as user_id_2) as connection\n" +
            "where not exists (select user_connection.connection_id from user_connection where user_connection.user_id_1=connection.user_id_1 and user_connection.user_id_2=connection.user_id_2)";
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
