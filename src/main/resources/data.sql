INSERT INTO post (id, title         , description               , content                                   , created_at        , updated_at        )
VALUE            (1 , 'Java advance', 'Learn Spring Framework'  , 'Learn Spring Framework with mentor Khoa' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP );

INSERT INTO comment (id, name           , email             , body                  , created_at       , updated_at         , post_id   )
VALUE               (1 , "Vũ Thế Nam"   , "nam.vt@gmail.com", "Bài viết rất bổ ích" , CURRENT_TIMESTAMP, CURRENT_TIMESTAMP  , 1         );