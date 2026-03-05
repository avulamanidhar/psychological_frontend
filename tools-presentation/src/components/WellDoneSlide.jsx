import BottomNav from './BottomNav.jsx'

/**
 * Reusable Well Done screen. Used by Self Care, Grounding, and Breathing sections.
 * NOTE: This component does not own navigation; the parent provides onBackToTools.
 */
export default function WellDoneSlide({ exerciseName, onBackToTools }) {
  return (
    <div className="bg-bg-splash min-h-screen flex flex-col">
      <div className="flex-1 px-6 pt-24 flex flex-col items-center">
        <div className="w-24 h-24 rounded-[24px] bg-complete-teal/60 flex items-center justify-center flex-shrink-0">
          <div className="w-12 h-12 rounded-full border-4 border-white/70 flex items-center justify-center">
            <span className="text-white text-2xl font-bold">✓</span>
          </div>
        </div>

        <h1 className="text-title-blue text-[34px] font-extrabold mt-7 text-center tracking-tight">
          Well done! 🎉
        </h1>
        <p className="text-desc-gray text-[18px] text-center mt-3 leading-6 max-w-sm">
          You completed the {exerciseName} exercise. How do you feel now?
        </p>

        <div className="flex gap-4 mt-10 w-full max-w-sm">
          {[
            { emoji: '😊', label: 'Better' },
            { emoji: '😐', label: 'Same' },
            { emoji: '😔', label: 'Still tough' },
          ].map((o) => (
            <div
              key={o.label}
              className="flex-1 bg-white rounded-2xl px-3 py-4 flex flex-col items-center border border-stroke-gray/60 shadow-[0_6px_16px_rgba(26,35,126,0.06)]"
            >
              <div className="w-12 h-12 rounded-full bg-gradient-to-b from-[#FFE08A] to-[#FFC44D] flex items-center justify-center shadow-[0_6px_12px_rgba(255,196,77,0.35)]">
                <span className="text-[22px]">{o.emoji}</span>
              </div>
              <span className="text-desc-gray text-[14px] font-semibold mt-2">{o.label}</span>
            </div>
          ))}
        </div>

        <button
          type="button"
          onClick={onBackToTools}
          className="w-full max-w-sm h-14 mt-10 rounded-2xl bg-button-blue text-white text-[18px] font-bold shadow-[0_14px_28px_rgba(74,144,226,0.35)]"
        >
          Back to Tools
        </button>
      </div>

      <BottomNav />
    </div>
  )
}
