package com.honoka.api.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class StableDiffusionApiConfigReq {

    private boolean samples_save;
    private String samples_format;
    private String samples_filename_pattern;
    private boolean save_images_add_number;
    private boolean grid_save;
    private String grid_format;
    private boolean grid_extended_filename;
    private boolean grid_only_if_multiple;
    private boolean grid_prevent_empty_spots;
    private int n_rows;
    private boolean enable_pnginfo;
    private boolean save_txt;
    private boolean save_images_before_face_restoration;
    private boolean save_images_before_highres_fix;
    private boolean save_images_before_color_correction;
    private int jpeg_quality;
    private boolean webp_lossless;
    private boolean export_for_4chan;
    private int img_downscale_threshold;
    private int target_side_length;
    private int img_max_size_mp;
    private boolean use_original_name_batch;
    private boolean use_upscaler_name_as_suffix;
    private boolean save_selected_only;
    private boolean do_not_add_watermark;
    private String temp_dir;
    private boolean clean_temp_dir_at_start;
    private String outdir_samples;
    private String outdir_txt2img_samples;
    private String outdir_img2img_samples;
    private String outdir_extras_samples;
    private String outdir_grids;
    private String outdir_txt2img_grids;
    private String outdir_img2img_grids;
    private String outdir_save;
    private boolean save_to_dirs;
    private boolean grid_save_to_dirs;
    private boolean use_save_to_dirs_for_ui;
    private String directories_filename_pattern;
    private int directories_max_prompt_words;
    private int ESRGAN_tile;
    private int ESRGAN_tile_overlap;
    private List<String> realesrgan_enabled_models;
    private String upscaler_for_img2img;
    private String face_restoration_model;
    private double code_former_weight;
    private boolean face_restoration_unload;
    private boolean show_warnings;
    private int memmon_poll_rate;
    private boolean samples_log_stdout;
    private boolean multiple_tqdm;
    private boolean print_hypernet_extra;
    private boolean unload_models_when_training;
    private boolean pin_memory;
    private boolean save_optimizer_state;
    private boolean save_training_settings_to_txt;
    private String dataset_filename_word_regex;
    private String dataset_filename_join_string;
    private int training_image_repeats_per_epoch;
    private int training_write_csv_every;
    private boolean training_xattention_optimizations;
    private boolean training_enable_tensorboard;
    private boolean training_tensorboard_save_images;
    private int training_tensorboard_flush_every;

    @JSONField(name = "sd_model_checkpoint")
    private String sdModelCheckpoint;

    private int sd_checkpoint_cache;
    private int sd_vae_checkpoint_cache;
    private String sd_vae;
    private boolean sd_vae_as_default;
    private int inpainting_mask_weight;
    private int initial_noise_multiplier;
    private boolean img2img_color_correction;
    private boolean img2img_fix_steps;
    private String img2img_background_color;
    private boolean enable_quantization;
    private boolean enable_emphasis;
    private boolean enable_batch_seeds;
    private int comma_padding_backtrack;
    private int CLIP_stop_at_last_layers;
    private boolean upcast_attn;
    private boolean use_old_emphasis_implementation;
    private boolean use_old_karras_scheduler_sigmas;
    private boolean no_dpmpp_sde_batch_determinism;
    private boolean use_old_hires_fix_width_height;
    private boolean interrogate_keep_models_in_memory;
    private boolean interrogate_return_ranks;
    private int interrogate_clip_num_beams;
    private int interrogate_clip_min_length;
    private int interrogate_clip_max_length;
    private int interrogate_clip_dict_limit;
    private List<String> interrogate_clip_skip_categories;
    private double interrogate_deepbooru_score_threshold;
    private boolean deepbooru_sort_alpha;
    private boolean deepbooru_use_spaces;
    private boolean deepbooru_escape;
    private String deepbooru_filter_tags;
    private String extra_networks_default_view;
    private int extra_networks_default_multiplier;
    private String extra_networks_add_text_separator;
    private String sd_hypernetwork;
    private boolean return_grid;
    private boolean do_not_show_images;
    private boolean add_model_hash_to_info;
    private boolean add_model_name_to_info;
    private boolean disable_weights_auto_swap;
    private boolean send_seed;
    private boolean send_size;
    private String font;
    private boolean js_modal_lightbox;
    private boolean js_modal_lightbox_initially_zoomed;
    private boolean show_progress_in_title;
    private boolean samplers_in_dropdown;
    private boolean dimensions_and_batch_together;
    private double keyedit_precision_attention;
    private double keyedit_precision_extra;
    private String quicksettings;
    private List<String> hidden_tabs;
    private String ui_reorder;
    private String ui_extra_networks_tab_reorder;
    private String localization;
    private boolean show_progressbar;
    private boolean live_previews_enable;
    private boolean show_progress_grid;
    private int show_progress_every_n_steps;
    private String show_progress_type;
    private String live_preview_content;
    private int live_preview_refresh_period;
    private List<String> hide_samplers;
    private int eta_ddim;
    private int eta_ancestral;
    private String ddim_discretize;
    private int s_churn;
    private int s_tmin;
    private int s_noise;
    private int eta_noise_seed_delta;
    private boolean always_discard_next_to_last_sigma;
    private String uni_pc_variant;
    private String uni_pc_skip_type;
    private int uni_pc_order;
    private boolean uni_pc_lower_order_final;
    private List<String> postprocessing_enable_in_main_ui;
    private List<String> postprocessing_operation_order;
    private int upscaling_max_images_in_cache;
    private List<String> disabled_extensions;
    private String sd_checkpoint_hash;
    private String sd_lora;
    private boolean lora_apply_to_outputs;

    public String getSdModelCheckpoint() {
        return sdModelCheckpoint;
    }

    public void setSdModelCheckpoint(String sdModelCheckpoint) {
        this.sdModelCheckpoint = sdModelCheckpoint;
    }
}
