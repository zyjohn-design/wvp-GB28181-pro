CREATE TABLE wvp_preset_remark (
    channel_id INTEGER NOT NULL,
    preset_id INTEGER NOT NULL,
    preset_name VARCHAR(128) NOT NULL,
    PRIMARY KEY (channel_id, preset_id)
);
