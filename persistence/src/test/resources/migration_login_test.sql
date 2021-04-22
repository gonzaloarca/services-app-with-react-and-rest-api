CREATE TABLE IF NOT EXISTS user_role
(
    user_id SERIAL NOT NULL,
    role_id INT    NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users,
    PRIMARY KEY (user_id, role_id)
);

DROP VIEW IF EXISTS full_post;
CREATE VIEW full_post AS
SELECT job_post.post_id,
       post_title,
       post_available_hours,
       post_job_type,
       post_is_active,
       user_id,
       user_email,
       user_name,
       user_phone,
       user_is_active,
       user_image,
       users.image_type                     as user_image_type,
       coalesce(avg(review_rate), 0)        AS rating,
       array_agg(DISTINCT zone_id)          as zones,
       count(distinct contract.contract_id) as contracts,
       count(DISTINCT review.contract_id)   as reviews
FROM job_post
         LEFT JOIN users ON job_post.user_id = users.user_id
         LEFT JOIN post_zone ON job_post.post_id = post_zone.post_id
         LEFT JOIN job_package ON job_post.post_id = job_package.post_id
         LEFT JOIN contract ON contract.package_id = job_package.package_id
         LEFT JOIN review ON review.contract_id = contract.contract_id
GROUP BY job_post.post_id, post_title, post_available_hours, post_job_type, post_is_active, job_post.user_id,
         user_email,
         user_name, user_phone, user_is_active, user_image, users.image_type;

-- DROP VIEW full_contract
CREATE VIEW full_contract AS
SELECT *
FROM contract
         NATURAL JOIN job_package
         NATURAL JOIN (SELECT post_id,
                           post_title,
                           post_job_type,
                           post_available_hours,
                           post_is_active,
                           ARRAY_AGG(DISTINCT zone_id) as zones,
                           user_id
                    FROM job_post
                             NATURAL JOIN post_zone
                    GROUP BY post_id, post_title, post_available_hours, post_job_type, post_is_active,
                             user_id) AS posts
         NATURAL JOIN (SELECT user_id        AS client_id,
                           user_email     AS client_email,
                           user_name      AS client_name,
                           user_phone     AS client_phone,
                           user_is_active as client_is_active,
                           user_image     as client_image,
                           image_type     as client_image_type
                    FROM users) AS clients
         NATURAL JOIN (SELECT user_id        AS professional_id,
                           user_email     AS professional_email,
                           user_name      AS professional_name,
                           user_phone     AS professional_phone,
                           user_is_active as professional_is_active,
                           user_image     as professional_image,
                           image_type     as professional_image_type
                    FROM users) AS professionals;

CREATE TABLE IF NOT EXISTS verification_token
(
    user_id       INT PRIMARY KEY,
    token         VARCHAR(100) NOT NULL,
    creation_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users
);
