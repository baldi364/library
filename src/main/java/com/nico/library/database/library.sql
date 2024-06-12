CREATE DATABASE library;
USE library;

SHOW TABLES;

-- INSERT TABELLA BOOK PRE-IMPOSTATA
INSERT INTO book (isbn, author, genre, plot, title)
VALUES
    ('9780451524935', 'George Orwell', 'Distopico', 'La Terra è divisa in tre enormi continenti: Oceania,Eurasia ed Estasia. Il comandante supremo è il Grande Fratello, un dittatore misterioso che nessuno ha mai visto.', '1984'),
    ('9780141439518', 'Jane Austen', 'Drammatico', 'I destini di Elizabeth, Jane, Mr Bingley e dell''ombroso Mr Darcy intrecciano un balletto irresistibile, una danza psicologica che getta luce sulla multiforme imprendibilità dell''animo umano.', 'Orgoglio e Pregiudizio'),
    ('9780066238500', 'C.S. Lewis', 'Fantasy', 'Peter, Susan, Edmund e Lucy sono quattro fratelli che vivono presso la villa di campagna di un insegnante in pensione, scoprendo che, attraverso un armadio nella casa del Professore, si può arrivare in un...', 'Cronache di Narnia'),
    ('9780743273565', 'F. Scott Fitzgerald', 'Drammatico', 'Il grande Gatsby ovvero l''età del jazz: luci, party, belle auto e vestiti da cocktail, ma dietro la tenerezza della notte si cela la sua oscurità, la sua durezza, il senso di solitudine con il quale può strangolare anche la vita più promettente.', 'Il Grande Gatsby'),
    ('9780747532743', 'J.K. Rowling', 'Fantasy', 'L''undicenne Harry Potter, rimasto orfano all''età di un anno, è stato cresciuto dai perfidi zii che non esitano a vessarlo e mortificarlo. Fino a quando, grazie ad una lettera che gli viene recapitata magicamente, non scopre la sua vera identità.', 'Harry Potter e la Pietra Filosofale'),
    ('9780142437230', 'Miguel de Cervantes', 'Satirico', 'Don Chisciotte è il simbolo della cieca fede in un ideale che resiste a qualunque oltraggio, il suo scudiero Sancho invece l''allegoria vivente del buon senso, della concretezza anche ingrata del reale.', 'Don Chisciotte'),
    ('9780156012195', 'Antoine de Saint-Exupéry', 'Filosofico', 'L''incontro tra un aviatore, rimasto in panne nel deserto del Sahara, e un ragazzo venuto da un piccolo pianeta lontano per sfuggire alla solitudine. L''indimenticabile storia di un''amicizia destinata a durare per sempre.', 'Il Piccolo Principe'),
    ('9780141439556', 'Emily Brontë', 'Drammatico', 'Un romanzo in cui domina la violenza sugli uomini, sugli animali, sulle cose, che narra il consumarsi di un''inesorabile vendetta portata avanti con fredda meticolosità dal disumano Heathcliff.', 'Cime tempestose'),
    ('9781407132082', 'Suzanne Collins', 'Fantasy', 'Katnisse e Peeta sono i vincitori degli Hunger Games e, come da tradizione, in seguito alla vittoria, i due dovranno effettuare una sorta di tour per essere acclamati dal popolo. I ragazzi, in realtà, non sono innamorati come fanno credere al pubblico.', 'La Ragazza di Fuoco'),
    ('1234567892', 'Stephenie Meyer', 'Fantasy', 'Storia d''amore tra un''umana e un vampiro', 'Twilight');


-- INSERT TABELLA AUTHORITY PRE-IMPOSTATA
INSERT INTO authority (authority_name, default_authority, `visible`) VALUES
("ROLE_ADMIN",0,1),
("ROLE_MEMBER",1,1);

----- QUERY

-- QUERY PRONTA PER SETTARE ROLE_ADMIN IN QUALSIASI USER REGISTRATO (sostituire ? con l'id dello user desiderato)
UPDATE user_authorities SET authority_id = 1 WHERE user_id = ?
 
-- QUERY PRONTA PER TROVARE UTENTI ED I LORO RUOLI
SELECT u.username, a.authority_name
FROM library_user u
LEFT JOIN user_authorities ua
ON u.user_id = ua.user_id
LEFT JOIN authority a
ON a.authority_id = ua.authority_id;

-- TUTTE LE SELECT
SELECT *
FROM authority;

SELECT *
FROM book;

SELECT *
FROM library_user;

SELECT *
FROM user_authorities;

SELECT *
FROM user_book;


  


