package com.chaos.badoo.searcher.badoo;

/**
 *  200, 210, 230, 310, 330, 530, 540, 340, 331, 290, 291, 550, 580, 670, 660
 *
 * 50 - profile_complete_percent: 71
 * 90 - location: {"$gpb":"badoo.bma.GeoLocation","display_image":"https://pd1eu.badoocdn.com/big/g/62/4247.png"}
 * 91 - country
 * 92 - region: {"$gpb":"badoo.bma.Region","id":291,"name":"Lviv Oblast"}
 * 93 - city: {"$gpb":"badoo.bma.City","id":4247,"name":"Lviv"}
 * 100 - verified_information
 *
 * 200 - name "Діана"
 * 210 - age 18
 * 220 - dob - mostly empty ""
 * 230 - gender 2
 * 240 - is_newbie true
 * 250 - is_deleted false
 * 260 - is_hot
 * 270 - is_invisible
 * 280 - is_unread
 * 290 - is_verified
 * 291 - verification_status 4
 *
 * 300 - is_extended_match
 * 301 - has_spp true
 * 303 - has_riseup: true
 * 304 - last_riseup_time_message: "17 minutes ago"
 * 310 - photo_count 3
 * 311 - video_count 0
 * 330 - online status 1
 * 331 - online_status_text: "Online now!"
 * 340 - preview_url, large_url, large_photo_size
 * 360 - wish: "Wants to date with guys, 18-28"
 *
 * 400 - interests_total 6
 * CODE: 420 interests: [{"$gpb":"badoo.bma.Interest","interest_id":793,"name":"Chilling","group_id":18,"is_matched":true},{"$gpb":"badoo.bma.Interest","interest_id":337713,"name":"Dancing","group_id":18},{"$gpb":"badoo.bma.Interest","interest_id":300,"name":"Clothes shopping","group_id":13},{"$gpb":"badoo.bma.Interest","interest_id":146646,"name":"Listening to Music","group_id":18},{"$gpb":"badoo.bma.Interest","interest_id":170,"name":"Horror films","group_id":12},{"$gpb":"badoo.bma.Interest","interest_id":1123497,"name":"Shopping","group_id":18}]
 *
 * 460 - social_networks
 * 480 - spoken_languages
 * 410 - interests_in_common
 * 471 - profile_score_numeric
 * 480 - spoken_languages: [{"$gpb":"badoo.bma.Language","uid":77,"name":"Ukrainian","level":0}]
 * 490 - profile_fields - About me, location, Relationship, living, appearance, children, languages, weight
 * 492 - displayed_about_me - I'm single, 160 cm, 58 kg, a few extra pounds, brown hair and black eyes, living with roommate(s)
 * 493 - profile_summary - mostly empty
 * 494 - tiw_idea - tiw_phrase_id: To see what happens
 *
 * 540 - distance_long
 * 550 - my_vote
 * 560 - their_vote - 1
 * 570 - match_message
 * 580 - is_match
 * 583 - is_crush
 * 586 - is_spark
 *
 * 600 - is_blocked
 * 601 - is_hidden
 * 610 - is_favourite
 * 620 - is_friend
 * 630 - is_conversation
 * 640 - unread_messages_count
 * 650 - allow_add_to_favourites
 * 660 - allow_chat
 * 661 - allow_quick_chat
 * 670 - allow_voting
 * 680 - has_mobile_app
 * 690 - popularity_level
 * 691 - popularity_pnb_place
 * 692 - popularity_visitors_today
 * 693 - popularity_visitors_month
 *
 * 730 - allow_smile
 * 731 - allow_send_gift
 * 732 - allow_crush: true
 * 733 - allow_spark: false
 * 750 - received_gifts: {"$gpb":"badoo.bma.ReceivedGifts","suggested_gift":{"$gpb":"badoo.bma.GiftProduct","product_id":3,"thumb_url":"//pd1eu.badoocdn.com/big/assets/gifts3/thumb/web/standard/sz___size__/coffee.png","large_url":"//pd1eu.badoocdn.com/big/assets/gifts3/normal/web/standard/sz___size__/coffee.png"}}
 * 760 - came_from: 2
 * 761 - came_from_text: "15 September 2019 from People nearby"
 * 762 - is_inapp_promo_partner: false
 * 770 - allow_sharing: true
 * 790 - is_highlighted: false
 *
 * 850 - encrypted_user_id: "oaac415fe6c2cbf627f6d54554bea82296d41f0adf5919dd4"
 * 860 - is_locked: true
 * 870 - promo_block_after_last_photo: {"$gpb":"badoo.bma.PromoBlock","mssg":"Why not send her a gift?","action":"Send A Gift","header":"Everyone loves presents","ok_action":63,"other_text":"","pictures":[{"$gpb":"badoo.bma.ApplicationFeaturePicture","display_images":"//pd1eu.badoocdn.com/big/assets/gifts3/normal/web/standard/sz___size__/rose.png"}],"promo_block_type":24,"promo_block_position":12,"credits_cost":"","redirect_page":{"$gpb":"badoo.bma.RedirectPage","redirect_page":98,"user_id":"oac50a4aaf276248d317084d9c3da6373d02af51f26f38d23"},"unique_id":"d4011e60415858480ab2ce020ed34b9c","stats_required":[1,2]}
 *
 * 900 - is_teleported: false
 * 930 - type: 0
 * 1110 - sections: [{"$gpb":"badoo.bma.UserSection","type":1},{"$gpb":"badoo.bma.UserSection","type":2},{"$gpb":"badoo.bma.UserSection","type":9},{"$gpb":"badoo.bma.UserSection","type":5},{"$gpb":"badoo.bma.UserSection","type":6},{"$gpb":"badoo.bma.UserSection","type":14},{"$gpb":"badoo.bma.UserSection","type":7},{"$gpb":"badoo.bma.UserSection","type":8},{"$gpb":"badoo.bma.UserSection","type":10},{"$gpb":"badoo.bma.UserSection","type":11},{"$gpb":"badoo.bma.UserSection","type":12}]
 * 1111 - edit_sections: [{"$gpb":"badoo.bma.UserSection","type":19},{"$gpb":"badoo.bma.UserSection","type":25},{"$gpb":"badoo.bma.UserSection","type":1},{"$gpb":"badoo.bma.UserSection","type":23},{"$gpb":"badoo.bma.UserSection","type":2},{"$gpb":"badoo.bma.UserSection","type":3},{"$gpb":"badoo.bma.UserSection","type":22},{"$gpb":"badoo.bma.UserSection","type":5},{"$gpb":"badoo.bma.UserSection","type":9},{"$gpb":"badoo.bma.UserSection","type":18},{"$gpb":"badoo.bma.UserSection","type":24},{"$gpb":"badoo.bma.UserSection","type":6}]
 * 1130 - lookalikes_info: {"$gpb":"badoo.bma.LookalikesInfo","message":"Discover Діана’s lookalikes","photo_id":"oeb29b068e700e8be9c733bc4d3aa27564da18a298ac0e3a71bb13ba93c4079ea9a0425cf11ab59832dd8f357b9988cab642891f833c8054920648697e871564c"}
 * 1162 - travel_location: {"$gpb":"badoo.bma.Location"}
 * 1210 - muted_until_timestamp: 0
 * 1251 - livestream_badge: 0
 * 1253 - livestream_followers_count: 0
 *
 * 1300 - contact_details_id: ""
 * 1422 - show_your_move_indicator: false
 */
public enum  UserField {

    NAME(200),
    AGE(210),
    IS_NEWBIE(240),
    IS_HOT(260),
    ONLINE_STATUS(330),
    PREVIEW_PHOTO(340),
    /**
     * Available only for request_albums
     */
    PHOTO_ALBUMS(370),
    INTERESTS(420),
    SOCIAL_NETWORKS(460),
    PROFILE_FIELDS(490),// < most interested
    DISPLAYED_ABOUT_ME(492),
    ALLOW_CHAT(660),
    ALLOW_QUICK_CHAT(661),
    POPULARITY_LEVEL(690),
    POPULARITY_PNB_PLACE(691),
    POPULARITY_VISITORS_TODAY(692),
    POPULARITY_VISITORS_MONTH(693)
    ;

    UserField(int code) {
        this.code = code;
    }

    private final int code;

    public int getCode() {
        return code;
    }
}
