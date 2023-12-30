package com.honoka.api.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class StableDiffusionApiReq {

    @JSONField(name = "enable_hr")
    private boolean enableHr;

    @JSONField(name = "denoising_strength")
    private int denoisingStrength;

    @JSONField(name = "firstphase_width")
    private int firstphaseWidth;

    @JSONField(name = "firstphase_height")
    private int firstphaseHeight;

    @JSONField(name = "hr_scale")
    private int hrScale;

    @JSONField(name = "hr_upscaler")
    private String hrUpscaler;

    @JSONField(name = "hr_second_pass_steps")
    private int hrSecondPassSteps;

    @JSONField(name = "hr_resize_x")
    private int hrResizeX;

    @JSONField(name = "hr_resize_y")
    private int hrResizeY;

    @JSONField(name = "prompt")
    private String prompt;

    @JSONField(name = "styles")
    private List<String> styles;

    @JSONField(name = "seed")
    private Long seed;

    @JSONField(name = "subseed")
    private int subseed;

    @JSONField(name = "subseed_strength")
    private int subseedStrength;

    @JSONField(name = "seed_resize_from_h")
    private int seedResizeFromH;

    @JSONField(name = "seed_resize_from_w")
    private int seedResizeFromW;

    @JSONField(name = "sampler_name")
    private String samplerName;

    @JSONField(name = "batch_size")
    private int batchSize;

    @JSONField(name = "n_iter")
    private int nIter;

    @JSONField(name = "steps")
    private int steps;

    @JSONField(name = "cfg_scale")
    private int cfgScale;

    @JSONField(name = "width")
    private int width;

    @JSONField(name = "height")
    private int height;

    @JSONField(name = "restore_faces")
    private boolean restoreFaces;

    @JSONField(name = "tiling")
    private boolean tiling;

    @JSONField(name = "do_not_save_samples")
    private boolean doNotSaveSamples;

    @JSONField(name = "do_not_save_grid")
    private boolean doNotSaveGrid;

    @JSONField(name = "negative_prompt")
    private String negativePrompt;

    @JSONField(name = "eta")
    private int eta;

    @JSONField(name = "s_churn")
    private int sChurn;

    @JSONField(name = "s_tmax")
    private int sTmax;

    @JSONField(name = "s_tmin")
    private int sTmin;

    @JSONField(name = "s_noise")
    private int sNoise;

    @JSONField(name = "override_settings")
    private Object overrideSettings;

    @JSONField(name = "override_settings_restore_afterwards")
    private boolean overrideSettingsRestoreAfterwards;

    @JSONField(name = "script_args")
    private List<String> scriptArgs;

    @JSONField(name = "sampler_index")
    private String samplerIndex;

    @JSONField(name = "script_name")
    private String scriptName;

    @JSONField(name = "send_images")
    private boolean sendImages;

    @JSONField(name = "save_images")
    private boolean saveImages;

    @JSONField(name = "alwayson_scripts")
    private Object alwaysonScripts;

    public boolean isEnableHr() {
        return enableHr;
    }

    public void setEnableHr(boolean enableHr) {
        this.enableHr = enableHr;
    }

    public int getDenoisingStrength() {
        return denoisingStrength;
    }

    public void setDenoisingStrength(int denoisingStrength) {
        this.denoisingStrength = denoisingStrength;
    }

    public int getFirstphaseWidth() {
        return firstphaseWidth;
    }

    public void setFirstphaseWidth(int firstphaseWidth) {
        this.firstphaseWidth = firstphaseWidth;
    }

    public int getFirstphaseHeight() {
        return firstphaseHeight;
    }

    public void setFirstphaseHeight(int firstphaseHeight) {
        this.firstphaseHeight = firstphaseHeight;
    }

    public int getHrScale() {
        return hrScale;
    }

    public void setHrScale(int hrScale) {
        this.hrScale = hrScale;
    }

    public String getHrUpscaler() {
        return hrUpscaler;
    }

    public void setHrUpscaler(String hrUpscaler) {
        this.hrUpscaler = hrUpscaler;
    }

    public int getHrSecondPassSteps() {
        return hrSecondPassSteps;
    }

    public void setHrSecondPassSteps(int hrSecondPassSteps) {
        this.hrSecondPassSteps = hrSecondPassSteps;
    }

    public int getHrResizeX() {
        return hrResizeX;
    }

    public void setHrResizeX(int hrResizeX) {
        this.hrResizeX = hrResizeX;
    }

    public int getHrResizeY() {
        return hrResizeY;
    }

    public void setHrResizeY(int hrResizeY) {
        this.hrResizeY = hrResizeY;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public List<String> getStyles() {
        return styles;
    }

    public void setStyles(List<String> styles) {
        this.styles = styles;
    }

    public Long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }

    public int getSubseed() {
        return subseed;
    }

    public void setSubseed(int subseed) {
        this.subseed = subseed;
    }

    public int getSubseedStrength() {
        return subseedStrength;
    }

    public void setSubseedStrength(int subseedStrength) {
        this.subseedStrength = subseedStrength;
    }

    public int getSeedResizeFromH() {
        return seedResizeFromH;
    }

    public void setSeedResizeFromH(int seedResizeFromH) {
        this.seedResizeFromH = seedResizeFromH;
    }

    public int getSeedResizeFromW() {
        return seedResizeFromW;
    }

    public void setSeedResizeFromW(int seedResizeFromW) {
        this.seedResizeFromW = seedResizeFromW;
    }

    public String getSamplerName() {
        return samplerName;
    }

    public void setSamplerName(String samplerName) {
        this.samplerName = samplerName;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getnIter() {
        return nIter;
    }

    public void setnIter(int nIter) {
        this.nIter = nIter;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getCfgScale() {
        return cfgScale;
    }

    public void setCfgScale(int cfgScale) {
        this.cfgScale = cfgScale;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isRestoreFaces() {
        return restoreFaces;
    }

    public void setRestoreFaces(boolean restoreFaces) {
        this.restoreFaces = restoreFaces;
    }

    public boolean isTiling() {
        return tiling;
    }

    public void setTiling(boolean tiling) {
        this.tiling = tiling;
    }

    public boolean isDoNotSaveSamples() {
        return doNotSaveSamples;
    }

    public void setDoNotSaveSamples(boolean doNotSaveSamples) {
        this.doNotSaveSamples = doNotSaveSamples;
    }

    public boolean isDoNotSaveGrid() {
        return doNotSaveGrid;
    }

    public void setDoNotSaveGrid(boolean doNotSaveGrid) {
        this.doNotSaveGrid = doNotSaveGrid;
    }

    public String getNegativePrompt() {
        return negativePrompt;
    }

    public void setNegativePrompt(String negativePrompt) {
        this.negativePrompt = negativePrompt;
    }

    public int getEta() {
        return eta;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }

    public int getsChurn() {
        return sChurn;
    }

    public void setsChurn(int sChurn) {
        this.sChurn = sChurn;
    }

    public int getsTmax() {
        return sTmax;
    }

    public void setsTmax(int sTmax) {
        this.sTmax = sTmax;
    }

    public int getsTmin() {
        return sTmin;
    }

    public void setsTmin(int sTmin) {
        this.sTmin = sTmin;
    }

    public int getsNoise() {
        return sNoise;
    }

    public void setsNoise(int sNoise) {
        this.sNoise = sNoise;
    }

    public Object getOverrideSettings() {
        return overrideSettings;
    }

    public void setOverrideSettings(Object overrideSettings) {
        this.overrideSettings = overrideSettings;
    }

    public boolean isOverrideSettingsRestoreAfterwards() {
        return overrideSettingsRestoreAfterwards;
    }

    public void setOverrideSettingsRestoreAfterwards(boolean overrideSettingsRestoreAfterwards) {
        this.overrideSettingsRestoreAfterwards = overrideSettingsRestoreAfterwards;
    }

    public List<String> getScriptArgs() {
        return scriptArgs;
    }

    public void setScriptArgs(List<String> scriptArgs) {
        this.scriptArgs = scriptArgs;
    }

    public String getSamplerIndex() {
        return samplerIndex;
    }

    public void setSamplerIndex(String samplerIndex) {
        this.samplerIndex = samplerIndex;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public boolean isSendImages() {
        return sendImages;
    }

    public void setSendImages(boolean sendImages) {
        this.sendImages = sendImages;
    }

    public boolean isSaveImages() {
        return saveImages;
    }

    public void setSaveImages(boolean saveImages) {
        this.saveImages = saveImages;
    }

    public Object getAlwaysonScripts() {
        return alwaysonScripts;
    }

    public void setAlwaysonScripts(Object alwaysonScripts) {
        this.alwaysonScripts = alwaysonScripts;
    }
}
